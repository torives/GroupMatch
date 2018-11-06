package br.com.yves.groupmatch.presentation.factory

import br.com.yves.groupmatch.data.showCalendar.DateRepositoryImpl

object DateRepositoryFactory {
	fun create() = DateRepositoryImpl()
}