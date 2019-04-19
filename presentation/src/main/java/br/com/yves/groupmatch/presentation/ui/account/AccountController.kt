package br.com.yves.groupmatch.presentation.ui.account

import android.content.Intent

interface AccountController {
	fun onViewCreated()
	fun onLoginAttempt()
	fun onLogoutAttempt()
	fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}