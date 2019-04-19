package br.com.yves.groupmatch.presentation.ui.account

import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.yves.groupmatch.presentation.GroupMatchApplication
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.ref.WeakReference


interface AuthenticationService {
	fun login()
	fun logoff()
	fun getUserData()
}

object GroupMatchAuth: AuthenticationService {
	private lateinit var activityReference: WeakReference<Context>
	private val context: Context
			get() {
				return activityReference.get()?.let { context ->
					context
				} ?: run {
					throw InitializationException()
				}
			}

	fun configure(activity: Context) {
		activityReference = WeakReference(activity)
	}

	//region AuthenticationService
	override fun login() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun logoff() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getUserData() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion

	class InitializationException: Exception(
			"${GroupMatchAuth::class.java.simpleName} was not initialized properly. You must call configure() passing an Activity before using this class methods"
	)
}

class AccountControllerImpl(private val view: AccountView) : AccountController {

	private lateinit var signInClient: GoogleSignInClient
	private lateinit var mAuth: FirebaseAuth

	//region AccountController
	override fun onViewCreated() {
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken("248519334188-b1dj36u4ei6en068qf7egk71trhval0a.apps.googleusercontent.com")
				.requestEmail()
				.build()
		signInClient = GoogleSignIn.getClient(GroupMatchApplication.instance, gso)
		mAuth = FirebaseAuth.getInstance()

		view.hideProgressBar()
		mAuth.currentUser?.let {
			view.showSignedInLayout(UserViewModel(("")))
		} ?: run {
			view.showSignedOffLayout()
		}
	}

	override fun onLoginAttempt() {
		view.startActivityForResult(signInClient.signInIntent, SIGNIN_REQUEST)
	}

	override fun onLogoutAttempt() {
		view.showSignedOffLayout()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == SIGNIN_REQUEST) {
			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
			handleSignInResult(task)
		}
	}
	//endregion

	private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
		try {
			val account = completedTask.getResult(ApiException::class.java)
			firebaseAuthWithGoogle(account!!)
		} catch (e: ApiException) {
			//TODO: Handle Exceptions
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			e.printStackTrace()
			Log.w(TAG, "signInResult:failed code=${e.statusCode}")
			view.showSignedOffLayout()
		}

	}

	private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

		val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener { task ->
					if (task.isSuccessful) {
						Log.d(TAG, "signInWithCredential:success")
						val user = mAuth.currentUser
						view.showSignedInLayout(UserViewModel(""))
					} else {
						Log.w(TAG, "signInWithCredential:failure", task.exception)
						view.showSignedOffLayout()
					}
				}
	}

	companion object {
		private val TAG = AccountControllerImpl::class.java.simpleName
		private const val SIGNIN_REQUEST = 100
	}
}