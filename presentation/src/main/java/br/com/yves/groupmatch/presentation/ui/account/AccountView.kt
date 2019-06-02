package br.com.yves.groupmatch.presentation.ui.account

interface AccountView {
	fun showProgressBar()
	fun hideProgressBar()
	fun showSignedInLayout(user: UserViewModel)
	fun showLoggedOutLayout()
}