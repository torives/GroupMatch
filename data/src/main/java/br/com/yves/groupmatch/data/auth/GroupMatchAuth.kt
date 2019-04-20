package br.com.yves.groupmatch.data.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.account.LoginCallback
import br.com.yves.groupmatch.domain.models.account.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.ref.WeakReference

class GroupMatchAuth private constructor() : AuthenticationService {
	private val firebaseAuth = FirebaseAuth.getInstance()
	private lateinit var activityReference: WeakReference<Context>
	private var loginCallback: LoginCallback? = null

	//FIXME: Guardar o IdToken em um lugar seguro
	private var googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken("248519334188-b1dj36u4ei6en068qf7egk71trhval0a.apps.googleusercontent.com")
			.requestEmail()
			.build()

	private val context: Context
		get() = activityReference.get() ?: throw InitializationException()


	//region AuthenticationService
	override fun login(callback: LoginCallback) {
		this.loginCallback = callback

		val signInClient = GoogleSignIn.getClient(context, googleSignInOptions)
		val authIntent = signInClient.signInIntent
		val intent = ProxyActivity.newIntent(context, authIntent)

		context.startActivity(intent)
	}

	override fun logoff() {
		firebaseAuth.signOut()
	}

	override fun getUser(): User? {
		return firebaseAuth.currentUser?.let {
			User(it.displayName!!)
		} ?: run {
			null
		}
	}
	//endregion

	internal fun onAuthResult(data: Intent) {
		val task = GoogleSignIn.getSignedInAccountFromIntent(data)
		try {
			val account = task.getResult(ApiException::class.java)
			firebaseAuthWithGoogle(account!!)
		} catch (e: ApiException) {
			//TODO: Handle Exceptions
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			loginCallback?.onFailure(e)
			loginCallback = null
//			Log.w(AccountControllerImpl.TAG, "signInResult:failed code=${e.statusCode}")
//			view.showSignedOffLayout()
		}
	}

	private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

		val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
		firebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener { task ->
					if (task.isSuccessful) {
						Log.d(TAG, "signInWithCredential:success")
						val user = firebaseAuth.currentUser
						//view.showSignedInLayout(UserViewModel(""))
						loginCallback?.onSuccess(User(user!!.displayName!!))
					} else {
						Log.w(TAG, "signInWithCredential:failure", task.exception)
						loginCallback?.onFailure(task.exception!!)
					}
					loginCallback = null
				}
	}

	companion object {
		val instance = GroupMatchAuth()
		private val TAG = GroupMatchAuth::class.java.simpleName

		fun setActivity(activity: Context) {
			instance.activityReference = WeakReference(activity)
		}
	}

	class InitializationException : Exception(
			"GroupMatchAuth was not initialized properly. You must call configure() passing an Activity before using this class methods"
	)
}