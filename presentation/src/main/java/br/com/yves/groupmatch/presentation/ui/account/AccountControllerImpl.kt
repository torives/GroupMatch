package br.com.yves.groupmatch.presentation.ui.account

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.account.LoginCallback
import br.com.yves.groupmatch.domain.models.account.User


class AccountControllerImpl(
		private val view: AccountView,
		private val authService: AuthenticationService
) : AccountController {

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
		authService.login(object : LoginCallback {
			override fun onSuccess(user: User) {
				view.showSignedInLayout(UserViewModel(user.name))
			}

			override fun onFailure(exception: Exception) {
				//TODO: handle error
				view.showSignedOffLayout()
			}

			override fun onCanceld() {
				//TODO: log
			}
		})
	}

	override fun onLogoutAttempt() {
		authService.logoff()
		view.showSignedOffLayout()
	}
	//endregion

	companion object {
		private val TAG = AccountControllerImpl::class.java.simpleName
	}
}