package br.com.yves.groupmatch.data.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.user.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.ref.WeakReference


class GroupMatchAuth private constructor(applicationContext: Context) : AuthenticationService, GoogleAuthenticationProxyActivity.GoogleAuthenticationProxyActivityListener {

	private val firebaseAuth = FirebaseAuth.getInstance()
	private var googleAuth: GoogleSignInClient
	private lateinit var activityReference: WeakReference<Context>
	private var loginCallback: AuthenticationService.LoginCallback? = null

	private val activityContext: Context
		get() = activityReference.get() ?: throw InitializationException()

	init {
		val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestScopes(Scope("https://www.googleapis.com/auth/calendar.events"))
				.requestServerAuthCode(applicationContext.getString(R.string.server_client_id))
				.requestIdToken(applicationContext.getString(R.string.server_client_id))
				.requestEmail()
				.build()
		googleAuth = GoogleSignIn.getClient(applicationContext, options)
	}

	//region AuthenticationService
	override fun login(callback: AuthenticationService.LoginCallback) {
		this.loginCallback = callback

		val authIntent = googleAuth.signInIntent
		val intent = GoogleAuthenticationProxyActivity.newIntent(activityContext, authIntent)
		GoogleAuthenticationProxyActivity.addListener(this)

		activityContext.startActivity(intent)
	}

	override fun logoff() {
		firebaseAuth.signOut()
		googleAuth.signOut()
	}

	override fun getLoggedUser(): User? {
		return FirebaseUserMapper.from(firebaseAuth.currentUser)
	}
	//endregion

	//region GoogleAuthenticationProxyActivityListener
	override fun onSuccessfulActivityRequest(requestCode: Int, data: Intent?) {
		val task = GoogleSignIn.getSignedInAccountFromIntent(data)
		try {
			task.getResult(ApiException::class.java)?.let { account ->
				firebaseLoginWithGoogle(account)
			} ?: run {
				val exception = GoogleAuthenticationException()
				Log.e(TAG, "Failed to authenticate with Google. ${exception.message}")
				loginCallback?.onFailure(exception)
			}
		} catch (exception: ApiException) {
			Log.e(TAG, "Failed to authenticate with Google. Error code=${exception.statusCode}")
			loginCallback?.onFailure(exception)
			loginCallback = null
		}
	}

	override fun onFailedActivityRequest(requestCode: Int) {
		loginCallback?.onCanceled()
		loginCallback = null
	}
	//endregion

	private fun firebaseLoginWithGoogle(account: GoogleSignInAccount) {
		val credential = GoogleAuthProvider.getCredential(account.idToken, null)
		firebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener { handleFirebaseLogin(it, account.serverAuthCode!!) }
	}

	private fun handleFirebaseLogin(task: Task<AuthResult>, authToken: String) {
		if (task.isSuccessful) {
			val user = FirebaseUserMapper.from(task.result?.user, authToken)
			user?.let { user ->
				Log.d(TAG, "Successful login with Google credentials")
				loginCallback?.onSuccess(user)
			} ?: run {
				Log.w(TAG, "Failed to login with Google credentials", task.exception)
				loginCallback?.onFailure(LoginException())
			}
		} else {
			Log.w(TAG, "Failed to login with Google credentials", task.exception)
			loginCallback?.onFailure(task.exception
					?: Exception("Failed to login with Google credentials"))
		}
		loginCallback = null
	}

	companion object {
		lateinit var instance: GroupMatchAuth
		private val TAG = GroupMatchAuth::class.java.simpleName

		fun init(applicationContext: Context) {
			instance = GroupMatchAuth(applicationContext)
		}

		fun setActivity(activity: Context) {
			instance.activityReference = WeakReference(activity)
		}
	}

	class LoginException : Exception("Failed to obtain user")
	class GoogleAuthenticationException : Exception("Failed to obtain Google account")
	class InitializationException : Exception(
			"GroupMatchAuth was not initialized properly. You must call configure() passing an Activity before using this class methods"
	)
}