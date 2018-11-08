package br.com.yves.groupmatch.presentation.factory

import br.com.yves.groupmatch.data.showCalendar.TimeSlotRepositoryImpl
import br.com.yves.groupmatch.presentation.Application

object TimeSlotRepositoryFactory {
	fun create() = TimeSlotRepositoryImpl(Application.INSTANCE)
}