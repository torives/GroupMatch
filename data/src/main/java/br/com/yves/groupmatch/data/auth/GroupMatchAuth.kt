package br.com.yves.groupmatch.data.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.domain.GroupMatchError
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.user.User
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.ref.WeakReference


class GroupMatchAuth private constructor(
		applicationContext: Context
) : AuthenticationService, GoogleAuthProxyActivity.Listener {

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
		val intent = GoogleAuthProxyActivity.newIntent(activityContext, authIntent)
		GoogleAuthProxyActivity.addListener(this)

		activityContext.startActivity(intent)
	}

	override fun logout() {
		firebaseAuth.signOut()
		googleAuth.signOut()
	}

	override fun getLoggedInUser(): User? {
		return firebaseAuth.currentUser?.let { FirebaseUserMapper.from(it) }
	}
	//endregion

	//region Listener
	override fun onSuccessfulActivityRequest(requestCode: Int, data: Intent?) {
		val task = GoogleSignIn.getSignedInAccountFromIntent(data)
		try {
			task.getResult(ApiException::class.java)?.let { account ->
				firebaseLoginWithGoogle(account)
			} ?: run {
				val error = GroupMatchAuthError.GoogleAuthenticationFailed()

				loginCallback?.onFailure(error)
				Log.e(TAG, error.message)
			}
		} catch (exception: ApiException) {
			if (exception.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
				loginCallback?.onCanceled()
				loginCallback = null
				Log.i(TAG, "Google SignIn cancelled")
			} else {
				val error = GroupMatchAuthError.GoogleAuthenticationFailed(exception)
				loginCallback?.onFailure(error)
				loginCallback = null
				Log.e(TAG, error.message, error.exception)
			}
		}
	}
	//endregion

	private fun firebaseLoginWithGoogle(account: GoogleSignInAccount) {
		val credential = GoogleAuthProvider.getCredential(account.idToken, null)
		firebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener { handleFirebaseLogin(it, account.serverAuthCode!!) }
	}

	private fun handleFirebaseLogin(task: Task<AuthResult>, authToken: String) {
		if (task.isSuccessful && task.result?.user != null) {
			val user = FirebaseUserMapper.from(task.result!!.user, authToken)
			user?.let { user ->
				Log.i(TAG, "Successful login with Google credentials")
				loginCallback?.onSuccess(user)
			}
		} else {
			val error = GroupMatchAuthError.FirebaseLoginFailed(task.exception)

			loginCallback?.onFailure(error)
			Log.e(TAG, error.message, task.exception)
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

	class InitializationException : Exception(
			"GroupMatchAuth was not initialized properly. You must call configure() passing an Activity before using this class methods"
	)
}


sealed class GroupMatchAuthError {
	class GoogleAuthenticationFailed(exception: Exception? = null) : GroupMatchError(1, "Failed to authenticate with Google", exception)
	class FirebaseLoginFailed(exception: Exception? = null) : GroupMatchError(2, "Failed to authenticate with Google", exception)
}