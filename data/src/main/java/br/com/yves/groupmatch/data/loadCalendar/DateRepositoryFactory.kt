package br.com.yves.groupmatch.data.loadCalendar

import br.com.yves.groupmatch.data.loadCalendar.DateRepositoryImpl

object DateRepositoryFactory {
	fun create() = DateRepositoryImpl()
}