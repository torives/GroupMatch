package br.com.yves.groupmatch.presentation.ui.account

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.user.APIError
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import br.com.yves.groupmatch.domain.user.UserRepositoryError

class AccountControllerImpl(
		private val view: AccountView,
		private val authService: AuthenticationService,
		private val userRepository: UserRepository
) : AccountController {

	private val userCallback = GetUserCallback()
	private val loginCallback = LoginCallback()

	//region AccountController
	override fun onViewCreated() {
		view.hideProgressBar()

		authService.getLoggedUser()?.let {
			val userViewModel = UserMapper.from(it)
			view.showSignedInLayout(userViewModel)
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

	inner class LoginCallback: AuthenticationService.LoginCallback {
		override fun onSuccess(loggedUser: User) {
			userRepository.getUser(loggedUser.id, userCallback)
		}

		override fun onFailure(exception: Exception) {
			//TODO: handle error
			view.showSignedOutLayout()
		}

		override fun onCanceled() {
			//TODO: log
		}
	}

	inner class GetUserCallback: UserRepository.GetUserCallback {
		override fun onSuccess(user: User) {
			val userViewModel = UserMapper.from(user)
			view.showSignedInLayout(userViewModel)
		}

		override fun onFailure(error: Error) {
			when(error) {
				is APIError -> {}
				is UserRepositoryError.InexistentUser -> {}
			}
		}
	}

	companion object {
		private val TAG = AccountControllerImpl::class.java.simpleName
	}
}