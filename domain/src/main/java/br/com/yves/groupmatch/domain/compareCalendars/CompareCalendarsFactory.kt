package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendar

object CompareCalendarsFactory {
	fun create(
			calendars: List<BusyCalendar>,
			createCalendar: CreateCalendar
	) = CompareCalendars(calendars, createCalendar)
}