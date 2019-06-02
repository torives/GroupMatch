package br.com.yves.groupmatch.presentation.ui.account

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository

class AccountControllerImpl(
		private val view: AccountView,
		private val authService: AuthenticationService,
		private val userRepository: UserRepository
) : AccountController {

	private val createUserCallback = CreateUserCallback()
	private val loginCallback = LoginCallback()

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
		authService.logoff()
		view.showSignedOutLayout()
	}
	//endregion

	private fun displaySignedInLayout(user: User) {
		val userViewModel = UserMapper.from(user)
		view.showSignedInLayout(userViewModel)
	}


	inner class LoginCallback : AuthenticationService.LoginCallback {

		override fun onSuccess(loggedUser: User) {
			userRepository.userExists(loggedUser.id, object : UserRepository.UserExistsCallback {
				override fun onUserExists() {
					displaySignedInLayout(loggedUser)
				}

				override fun onUserDoesNotExists() {
					userRepository.createUser(loggedUser, createUserCallback)
				}
			})
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