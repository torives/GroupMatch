package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.domain.showCalendar.ShowCalendar

object ShowCalendarFactory {
	fun create() = ShowCalendar(
		DateRepositoryFactory.create(),
		TimeSlotRepositoryFactory.create()
	)
}