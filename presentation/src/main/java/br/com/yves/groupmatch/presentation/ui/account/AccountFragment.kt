package br.com.yves.groupmatch.presentation.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.navigation.fragment.NavHostFragment
import br.com.yves.groupmatch.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_account_signedin.*
import kotlinx.android.synthetic.main.layout_account_signedout.*


data class UserViewModel(val name: String)

interface AccountView {
	fun showProgressBar()
	fun hideProgressBar()
	fun showSignedInLayout(user: UserViewModel)
	fun showSignedOffLayout()
}

interface AccountController {
	fun onViewCreated()
	fun onLoginAttempt()
	fun onLogoutAttempt()
}

class AccountControllerImpl(private val view: AccountView): AccountController {

	override fun onViewCreated() {
		view.showSignedInLayout(UserViewModel(("")))
		view.hideProgressBar()
	}

	override fun onLoginAttempt() {
		view.showProgressBar()
		view.showSignedInLayout(UserViewModel(""))
		view.hideProgressBar()
	}

	override fun onLogoutAttempt() {
		view.showProgressBar()
		view.showSignedOffLayout()
		view.hideProgressBar()
	}
}

object AccountControllerFabric {
	fun create(view: AccountView) = AccountControllerImpl(view)
}

class AccountFragment : NavHostFragment(), AccountView {

	private lateinit var signInClient: GoogleSignInClient
	private lateinit var mAuth: FirebaseAuth
	private lateinit var accountController: AccountController

	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_account, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		accountController = AccountControllerFabric.create(this)
		accountController.onViewCreated()

//		account_signInButton.setOnClickListener(this)
//
//		context?.let { context ->
//
//			val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//					.requestIdToken("248519334188-b1dj36u4ei6en068qf7egk71trhval0a.apps.googleusercontent.com")
//					.requestEmail()
//					.build()
//			signInClient = GoogleSignIn.getClient(context, gso)
//			mAuth = FirebaseAuth.getInstance()
//
//			mAuth.currentUser?.let {
//				//TODO: Display logged in layout
//			} ?: run {
//				//TODO: Display logged out layout
//			}
//		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

//		// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//		if (requestCode == SIGNIN_REQUEST) {
//			// The Task returned from this call is always completed, no need to attach
//			// a listener.
//			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//			handleSignInResult(task)
//		}
	}

	//region AccountView
	override fun showProgressBar() {
		account_progressBar.visibility = VISIBLE
	}

	override fun hideProgressBar() {
		account_progressBar.visibility = GONE
	}

	override fun showSignedInLayout(user: UserViewModel) {
		account_content.removeAllViews()
		inflateLayout(R.layout.layout_account_signedin)
		configureLogoutButton()
	}

	override fun showSignedOffLayout() {
		account_content.removeAllViews()
		inflateLayout(R.layout.layout_account_signedout)
		configureLoginButton()
	}
	//endregion

	private fun inflateLayout(@LayoutRes id: Int) {
		context?.let { context ->
			LayoutInflater.from(context)
					.inflate(id, account_content)
		}
	}

	private fun configureLoginButton(){
		account_signInButton!!.setOnClickListener {
			accountController.onLoginAttempt()
		}
	}

	private fun configureLogoutButton() {
		account_signedin_signoffButton!!.setOnClickListener {
			accountController.onLogoutAttempt()
		}
	}

	private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//		try {
//			val account = completedTask.getResult(ApiException::class.java)
//
//			// Signed in successfully, show authenticated UI.
//			firebaseAuthWithGoogle(account!!)
//		} catch (e: ApiException) {
//			// The ApiException status code indicates the detailed failure reason.
//			// Please refer to the GoogleSignInStatusCodes class reference for more information.
//			e.printStackTrace()
//			Log.w(TAG, "signInResult:failed code=${e.statusCode}")
//			//TODO: updateUI(null)
//		}

	}

	private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
//
//		val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//		mAuth.signInWithCredential(credential)
//				.addOnCompleteListener { task ->
//					if (task.isSuccessful) {
//						// Sign in success, update UI with the signed-in user's information
//						Log.d(TAG, "signInWithCredential:success")
//						val user = mAuth.currentUser
////						updateUI(user)
//					} else {
//						// If sign in fails, display a message to the user.
//						Log.w(TAG, "signInWithCredential:failure", task.exception)
////						Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
////						updateUI(null)
//					}
//				}
	}

//	override fun onClick(v: View?) {
//		when (v?.id) {
//			R.id.account_signInButton -> signIn()
//		}
//	}

	private fun signIn() {
//		startActivityForResult(signInClient.signInIntent, SIGNIN_REQUEST)
	}

	companion object {
		private val TAG = AccountFragment::class.java.simpleName
		private const val SIGNIN_REQUEST = 100
	}
}