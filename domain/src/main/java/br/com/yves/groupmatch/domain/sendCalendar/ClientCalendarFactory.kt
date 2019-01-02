package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.loadCalendar.Calendar

object ClientCalendarFactory {
	fun create(calendar: Calendar, owner: String) = ClientCalendar(
			owner,
			calendar.first().date,
			calendar.last().date,
			calendar.mapNotNull { if(it.isBusy) it.date else null }
	)
}