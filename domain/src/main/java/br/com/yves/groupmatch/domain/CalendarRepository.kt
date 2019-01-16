package br.com.yves.groupmatch.domain

import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.calendar.Calendar

interface CalendarRepository {
	fun insert(calendar: Calendar)
	fun update(calendar: Calendar)
	fun delete(calendar: Calendar)
	fun getCalendar(week: Week): Calendar?
}