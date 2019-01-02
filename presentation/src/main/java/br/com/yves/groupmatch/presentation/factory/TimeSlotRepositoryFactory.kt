package br.com.yves.groupmatch.presentation.factory

import br.com.yves.groupmatch.data.loadCalendar.TimeSlotRepositoryImpl
import br.com.yves.groupmatch.presentation.Application

object TimeSlotRepositoryFactory {
	fun create() = TimeSlotRepositoryImpl(Application.instance)
}