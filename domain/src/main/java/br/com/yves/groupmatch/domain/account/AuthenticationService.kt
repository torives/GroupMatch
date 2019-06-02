package br.com.yves.groupmatch.domain.account

import br.com.yves.groupmatch.domain.user.User

interface AuthenticationService {
	fun login(callback: LoginCallback)
	fun logout()
	fun getLoggedInUser(): User?

	interface LoginCallback {
		fun onSuccess(loggedUser: User)
		fun onFailure(exception: Exception)
		fun onCanceled()
	}
}
