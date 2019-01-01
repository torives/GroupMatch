package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar
import br.com.yves.groupmatch.domain.sendCalendar.ClientCalendar

object CompareCalendarsFactory {
	fun create(
			calendars: List<ClientCalendar>,
			createCalendar: CreateCalendar
	) = CompareCalendars(calendars, createCalendar)
}