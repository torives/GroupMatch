package br.com.yves.groupmatch.presentation.factory.updateCalendar

import br.com.yves.groupmatch.domain.updateCalendar.UpdateCalendar
import br.com.yves.groupmatch.presentation.factory.TimeSlotRepositoryFactory

object UpdateCalendarFactory {
	fun create() = UpdateCalendar(TimeSlotRepositoryFactory.create())
}
