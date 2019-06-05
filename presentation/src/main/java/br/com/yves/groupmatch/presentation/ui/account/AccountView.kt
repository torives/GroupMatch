package br.com.yves.groupmatch.presentation.ui.account

import br.com.yves.groupmatch.presentation.ui.ErrorViewModel

interface AccountView {
	fun showProgressBar()
	fun hideProgressBar()
	fun showSignedInLayout(user: UserViewModel)
	fun showLoggedOutLayout()
	fun showError(error: ErrorViewModel)
}
