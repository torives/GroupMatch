package br.com.yves.groupmatch.presentation.ui.account

import br.com.yves.groupmatch.domain.GroupMatchError
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.user.Tokens
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import br.com.yves.groupmatch.presentation.ui.ErrorViewModel
import com.google.firebase.iid.FirebaseInstanceId


interface AccountPresenter {
	fun format(error: Error): ErrorViewModel
}

class AccountPresenterImpl : AccountPresenter {
	override fun format(error: Error): ErrorViewModel {
		val gmError = error as GroupMatchError
		return ErrorViewModel(
				gmError.code,
				gmError.message
		)
	}
}

class AccountControllerImpl(
		private val view: AccountView,
		private val authService: AuthenticationService,
		private val userRepository: UserRepository,
		private val presenter: AccountPresenter
) : AccountController {

	private val loginCallback = LoginCallback()
	private val createUserCallback = CreateUserCallback()

	//region AccountController
	override fun onViewCreated() {
		view.hideProgressBar()

		authService.getLoggedInUser()?.let {
			displaySignedInLayout(it)
		} ?: view.showLoggedOutLayout()
	}

	override fun onLoginAttempt() {
		authService.login(loginCallback)
	}

	override fun onLogoutAttempt() {
		logout()
	}
	//endregion

	private fun displaySignedInLayout(user: User) {
		val userViewModel = UserMapper.from(user)
		view.showSignedInLayout(userViewModel)
	}

	private fun logout() {
		authService.logout()
		view.showLoggedOutLayout()
	}

	private fun displayError(error: Error) {
		val errorViewModel = presenter.format(error)
		view.showError(errorViewModel)
	}


	inner class LoginCallback : AuthenticationService.LoginCallback {

		override fun onSuccess(loggedUser: User) {
			userRepository.userExists(loggedUser.id, UserExistsCallback(loggedUser))
		}

		override fun onFailure(error: Error) {
			view.showLoggedOutLayout()
		}

		override fun onCanceled() {
			view.showLoggedOutLayout()
		}
	}

	inner class UserExistsCallback(private val user: User) : UserRepository.UserExistsCallback {
		override fun onUserExists() {
			displaySignedInLayout(user)
		}

		override fun onUserDoesNotExists() {
			FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
				user.tokens[Tokens.device] = it.token
				userRepository.createUser(user, createUserCallback)
			}.addOnFailureListener {
				logout()
			}
		}

		override fun onFailure(error: Error) {
			logout()
		}
	}

	inner class CreateUserCallback : UserRepository.CreateUserCallback {
		override fun onSuccess(user: User) {
			displaySignedInLayout(user)
		}

		//TODO: Log failure
		override fun onFailure(error: Error) {
			logout()
		}
	}

	companion object {
		private val TAG = AccountControllerImpl::class.java.simpleName
	}
}