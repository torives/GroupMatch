package br.com.yves.groupmatch.presentation.factory.updateCalendar

import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.updateCalendar.UpdateCalendar
import br.com.yves.groupmatch.data.loadCalendar.DateRepositoryFactory

object UpdateCalendarFactory {
	fun create(calendarRepository: CalendarRepository) = UpdateCalendar(calendarRepository)
}
