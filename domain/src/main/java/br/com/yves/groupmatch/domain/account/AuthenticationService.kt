package br.com.yves.groupmatch.domain.account

import br.com.yves.groupmatch.domain.models.account.User

interface AuthenticationService {
	fun login(callback: LoginCallback)
	fun logoff()
	fun getUser(): User?
}