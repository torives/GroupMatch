package br.com.yves.groupmatch.domain.saveCalendar

import br.com.yves.groupmatch.domain.CalendarRepository

object SaveCalendarFactory {
	fun create(calendarRepository: CalendarRepository) = SaveCalendar(calendarRepository)
}