package br.com.yves.groupmatch.domain

interface CalendarRepository {
	fun add(calendar: Calendar)
	fun update(week: Week, slots: Collection<TimeSlot>)
	fun delete(calendar: Calendar)
	fun getCalendar(week: Week): Calendar?
}