package br.com.yves.groupmatch.presentation.ui.account

import android.content.Intent

interface AccountController {
	fun onViewCreated()
	fun onLoginAttempt()
	fun onLogoutAttempt()
}