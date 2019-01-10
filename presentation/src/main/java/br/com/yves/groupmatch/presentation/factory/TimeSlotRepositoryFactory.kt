package br.com.yves.groupmatch.presentation.factory

import br.com.yves.groupmatch.data.loadCalendar.CalendarRepositoryImpl
import br.com.yves.groupmatch.presentation.Application

object TimeSlotRepositoryFactory {
	fun create() = CalendarRepositoryImpl(Application.instance)
}