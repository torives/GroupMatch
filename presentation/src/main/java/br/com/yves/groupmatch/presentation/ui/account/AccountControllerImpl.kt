package br.com.yves.groupmatch.presentation.ui.account

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.user.User

class AccountControllerImpl(
		private val view: AccountView,
		private val authService: AuthenticationService
) : AccountController {

	//region AccountController
	override fun onViewCreated() {
		view.hideProgressBar()

		authService.getLoggedUser()?.let {
			val userViewModel = UserMapper.from(it)
			view.showSignedInLayout(userViewModel)
		} ?: view.showSignedOutLayout()
	}

	override fun onLoginAttempt() {
		authService.login(object : AuthenticationService.LoginCallback {
			override fun onSuccess(user: User) {
				val userViewModel = UserMapper.from(user)
				view.showSignedInLayout(userViewModel)
			}

			override fun onFailure(exception: Exception) {
				//TODO: handle error
				view.showSignedOutLayout()
			}

			override fun onCanceled() {
				//TODO: log
			}
		})
	}

	override fun onLogoutAttempt() {
		authService.logoff()
		view.showSignedOutLayout()
	}
	//endregion

	companion object {
		private val TAG = AccountControllerImpl::class.java.simpleName
	}
}