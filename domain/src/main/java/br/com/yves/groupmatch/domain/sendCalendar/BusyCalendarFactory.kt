package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.showCalendar.Calendar

object BusyCalendarFactory {
	fun create(calendar: Calendar) = BusyCalendar(
			calendar.first().date,
			calendar.last().date,
			calendar.filter { it.isBusy }
	)
}