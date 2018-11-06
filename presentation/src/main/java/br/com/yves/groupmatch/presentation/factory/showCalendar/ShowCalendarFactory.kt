package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.domain.showCalendar.ShowCalendar
import br.com.yves.groupmatch.presentation.factory.common.TimeSlotRepositoryFactory

object ShowCalendarFactory {
	fun create() = ShowCalendar(
		DateRepositoryFactory.create(),
		TimeSlotRepositoryFactory.create()
	)
}