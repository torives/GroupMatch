package br.com.yves.groupmatch.presentation.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import br.com.yves.groupmatch.domain.models.account.User
import br.com.yves.groupmatch.presentation.GroupMatchApplication
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.ref.WeakReference


interface AuthenticationService {
	fun login()
	fun logoff()
	fun getUser(): User?
}

class ProxyActivity : Activity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val authIntent: Intent = this.intent.getParcelableExtra(AUTH_INTENT)
		this.startActivityForResult(authIntent, AUTH_REQUEST)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == AUTH_REQUEST && data != null) {
			GroupMatchAuth.onAuthResult(data)
			finish()
		}
	}

	companion object {
		private const val AUTH_REQUEST = 100
		private const val AUTH_INTENT = "br.com.yves.groupmatch.authIntent"
		private const val AUTH_RESULT_INTENT = "br.com.yves.groupmatch.authResultIntent"

		fun newIntent(context: Context, authIntent: Intent): Intent {
			return Intent(context, ProxyActivity::class.java).apply {
				putExtra(AUTH_INTENT, authIntent)
			}
		}
	}
}

object GroupMatchAuth : AuthenticationService {
	private val signInClient: GoogleSignInClient
	private val firebaseAuth = FirebaseAuth.getInstance()
	private lateinit var activityReference: WeakReference<Context>
	private val context: Context
		get() {
			return activityReference.get()?.let { context ->
				context
			} ?: run {
				throw InitializationException()
			}
		}
	private val TAG = GroupMatchAuth::class.java.simpleName


	init {
		//FIXME: Guardar o IdToken em um lugar seguro
		val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken("248519334188-b1dj36u4ei6en068qf7egk71trhval0a.apps.googleusercontent.com")
				.requestEmail()
				.build()
		signInClient = GoogleSignIn.getClient(GroupMatchApplication.instance, options)
	}

	fun configure(activity: Context) {
		activityReference = WeakReference(activity)
	}

	//region AuthenticationService
	override fun login() {
		val authIntent = signInClient.signInIntent
		val intent = ProxyActivity.newIntent(context, authIntent)
		context.startActivity(intent)
	}

	override fun logoff() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getUser(): User? {
		return null
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
			e.printStackTrace()
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
					} else {
						Log.w(TAG, "signInWithCredential:failure", task.exception)
						//view.showSignedOffLayout()
					}
				}
	}

	class InitializationException : Exception(
			"${GroupMatchAuth::class.java.simpleName} was not initialized properly. You must call configure() passing an Activity before using this class methods"
	)
}

class AccountControllerImpl(
		private val view: AccountView,
		private val authService: AuthenticationService
) : AccountController {

	private lateinit var signInClient: GoogleSignInClient
	private lateinit var mAuth: FirebaseAuth

	//region AccountController
	override fun onViewCreated() {
		view.hideProgressBar()
		authService.getUser()?.let {
			view.showSignedInLayout(UserViewModel(("")))
		} ?: run {
			view.showSignedOffLayout()
		}
	}

	override fun onLoginAttempt() {
		authService.login()
	}

	override fun onLogoutAttempt() {
		view.showSignedOffLayout()
	}
	//endregion

	companion object {
		private val TAG = AccountControllerImpl::class.java.simpleName
		private const val SIGNIN_REQUEST = 100
	}
}