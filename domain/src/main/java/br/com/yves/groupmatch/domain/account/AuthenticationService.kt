package br.com.yves.groupmatch.domain.account

import br.com.yves.groupmatch.domain.user.User

interface AuthenticationService {
	fun login(callback: LoginCallback)
	fun logoff()
	fun getLoggedUser(): User?

	interface LoginCallback {
		fun onSuccess(user: User)
		fun onFailure(exception: Exception)
		fun onCanceled()
	}
}
