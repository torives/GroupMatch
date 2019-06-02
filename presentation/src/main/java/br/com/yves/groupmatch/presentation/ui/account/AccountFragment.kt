package br.com.yves.groupmatch.presentation.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.factory.account.AccountControllerFactory
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.SignInButton
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment(), AccountView {

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

		accountController = AccountControllerFactory.create(this)

		runOnBackground {
			accountController.onViewCreated()
		}
	}

	//region AccountView
	override fun showProgressBar() = runOnUiThread {
		account_progressBar.visibility = VISIBLE
	}

	override fun hideProgressBar() = runOnUiThread {
		account_progressBar.visibility = GONE
	}

	override fun showSignedInLayout(user: UserViewModel) = runOnUiThread {
		account_content.removeAllViews()
		inflateLayout(R.layout.layout_account_signedin)
		populateSignInLayout(user)
		setLogoutButtonOnClickListener()
	}

	override fun showLoggedOutLayout() = runOnUiThread {
		account_content.removeAllViews()
		inflateLayout(R.layout.layout_account_signedout)
		setLoginButtonOnClickListener()
	}
	//endregion

	private fun inflateLayout(@LayoutRes id: Int) {
		context?.let { context ->
			LayoutInflater.from(context)
					.inflate(id, account_content)
		}
	}

	private fun populateSignInLayout(user: UserViewModel) {
		val nameEditText: EditText? = view?.findViewById(R.id.account_signedin_name)
		val emailEditText: EditText? = view?.findViewById(R.id.account_signedin_email)
		val profileImage: ImageView? = view?.findViewById(R.id.account_signedin_profilePicture)

		user.apply {
			nameEditText?.setText(name)
			emailEditText?.setText(email)

			profileImage?.let {
				Glide.with(this@AccountFragment)
						.load(profileImageURL)
						.apply(RequestOptions.circleCropTransform())
						.into(profileImage)
			}
		}
	}

	private fun setLoginButtonOnClickListener() {
		val signInButton = view?.findViewById<SignInButton>(R.id.account_signInButton)
		signInButton?.setOnClickListener {
			runOnBackground {
				accountController.onLoginAttempt()
			}
		}
	}

	private fun setLogoutButtonOnClickListener() {
		val signOffButton = view?.findViewById<Button>(R.id.account_signedin_signoutButton)
		signOffButton?.setOnClickListener {
			runOnBackground {
				accountController.onLogoutAttempt()
			}
		}
	}

	companion object {
		private val TAG = AccountFragment::class.java.simpleName
	}
}