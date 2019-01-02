package br.com.yves.groupmatch.presentation.factory

import br.com.yves.groupmatch.data.loadCalendar.DateRepositoryImpl

object DateRepositoryFactory {
	fun create() = DateRepositoryImpl()
}