package br.com.yves.groupmatch.presentation.ui.account

import android.content.Intent

interface AccountView {
	fun showProgressBar()
	fun hideProgressBar()
	fun showSignedInLayout(user: UserViewModel)
	fun showSignedOffLayout()
	fun startActivityForResult(intent: Intent, code: Int)
}