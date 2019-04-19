package br.com.yves.groupmatch.domain.loadCalendar

import br.com.yves.groupmatch.domain.models.account.User

interface AccountRepository {
	fun getLoggedUser(): User
}