package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.DateRepository

object CompareCalendarsFactory {
	fun create(
			calendars: List<Calendar>,
			dateRepository: DateRepository
	) = CompareCalendars(calendars, dateRepository)
}