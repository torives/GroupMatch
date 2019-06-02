package br.com.yves.groupmatch.presentation.ui.account

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository

class AccountControllerImpl(
		private val view: AccountView,
		private val authService: AuthenticationService,
		private val userRepository: UserRepository
) : AccountController {

	private val loginCallback = LoginCallback()
	private val createUserCallback = CreateUserCallback()

	//region AccountController
	override fun onViewCreated() {
		view.hideProgressBar()

		authService.getLoggedUser()?.let {
			displaySignedInLayout(it)
		} ?: view.showSignedOutLayout()
	}

	override fun onLoginAttempt() {
		authService.login(loginCallback)
	}

	override fun onLogoutAttempt() {
		logoff()
	}
	//endregion

	private fun displaySignedInLayout(user: User) {
		val userViewModel = UserMapper.from(user)
		view.showSignedInLayout(userViewModel)
	}

	private fun logoff() {
		authService.logoff()
		view.showSignedOutLayout()
	}


	inner class LoginCallback : AuthenticationService.LoginCallback {

		override fun onSuccess(loggedUser: User) {
			userRepository.userExists(loggedUser.id, UserExistsCallback(loggedUser))
		}

		//TODO: handle login failure
		override fun onFailure(exception: Exception) {
			view.showSignedOutLayout()
		}

		//TODO: handle login cancellation
		override fun onCanceled() {
			view.showSignedOutLayout()
		}
	}

	inner class UserExistsCallback(private val user: User): UserRepository.UserExistsCallback {
		override fun onUserExists() {
			displaySignedInLayout(user)
		}

		override fun onUserDoesNotExists() {
			userRepository.createUser(user, createUserCallback)
		}

		override fun onFailure(error: Error) {
			authService.logoff()
			view.showSignedOutLayout()
		}
	}

	inner class CreateUserCallback : UserRepository.CreateUserCallback {
		override fun onSuccess(user: User) {
			displaySignedInLayout(user)
		}

		//TODO: Log failure
		override fun onFailure(error: Error) {
			authService.logoff()
			view.showSignedOutLayout()
		}
	}

	companion object {
		private val TAG = AccountControllerImpl::class.java.simpleName
	}
}