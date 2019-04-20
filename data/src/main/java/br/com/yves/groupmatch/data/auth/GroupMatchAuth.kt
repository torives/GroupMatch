package br.com.yves.groupmatch.data.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.account.LoginCallback
import br.com.yves.groupmatch.domain.models.account.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.ref.WeakReference


class GroupMatchAuth private constructor(applicationContext: Context) : AuthenticationService {
	private val firebaseAuth = FirebaseAuth.getInstance()
	private lateinit var activityReference: WeakReference<Context>
	private var loginCallback: LoginCallback? = null

	private var googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestScopes(Scope("https://www.googleapis.com/auth/calendar.events"))
			.requestIdToken(applicationContext.getString(R.string.server_client_id))
			.requestEmail()
			.build()

	private val activityContext: Context
		get() = activityReference.get() ?: throw InitializationException()


	//region AuthenticationService
	override fun login(callback: LoginCallback) {
		this.loginCallback = callback

		val signInClient = GoogleSignIn.getClient(activityContext, googleSignInOptions)
		val authIntent = signInClient.signInIntent
		val intent = GoogleAuthenticationProxyActivity.newIntent(activityContext, authIntent)

		activityContext.startActivity(intent)
	}

	override fun logoff() {
		firebaseAuth.signOut()
	}

	override fun getUser(): User? {
		return FirebaseUserMapper.from(firebaseAuth.currentUser)
	}
	//endregion

	internal fun onGoogleAuthenticationResult(data: Intent) {
		val task = GoogleSignIn.getSignedInAccountFromIntent(data)
		try {
			task.getResult(ApiException::class.java)?.let { account ->
				firebaseLoginWithGoogle(account)
			} ?: run {
				val exception = GoogleAuthenticationException()
				Log.w(TAG, "Failed to authenticate with Google. ${exception.message}")
				loginCallback?.onFailure(exception)
			}
		} catch (exception: ApiException) {
			Log.w(TAG, "Failed to authenticate with Google. Error code=${exception.statusCode}")
			loginCallback?.onFailure(exception)
			loginCallback = null
		}
	}

	private fun firebaseLoginWithGoogle(account: GoogleSignInAccount) {
		//TODO: send auth code to server
		val credential = GoogleAuthProvider.getCredential(account.idToken, null)
		firebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener { handleFirebaseLogin(it) }
	}

	private fun handleFirebaseLogin(task: Task<AuthResult>) {
		if (task.isSuccessful) {
			val user = FirebaseUserMapper.from(task.result?.user)
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