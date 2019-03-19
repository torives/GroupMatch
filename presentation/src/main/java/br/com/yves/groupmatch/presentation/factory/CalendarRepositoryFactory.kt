package br.com.yves.groupmatch.presentation.factory

import br.com.yves.groupmatch.data.loadCalendar.CalendarRepositoryImpl

object CalendarRepositoryFactory {
	fun create() = CalendarRepositoryImpl()
}