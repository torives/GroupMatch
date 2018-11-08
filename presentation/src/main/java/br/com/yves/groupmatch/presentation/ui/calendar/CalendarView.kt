package br.com.yves.groupmatch.presentation.ui.calendar

interface CalendarView {
	fun showCalendar(calendar: CalendarViewModel)
	fun showLoading()
}