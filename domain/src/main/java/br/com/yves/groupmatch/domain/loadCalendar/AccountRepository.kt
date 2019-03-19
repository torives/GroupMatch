package br.com.yves.groupmatch.domain.loadCalendar

interface AccountRepository {
	fun getLoggedUser(): User
}