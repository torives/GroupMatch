package br.com.yves.groupmatch.ui.calendar

interface CalendarView {
    fun showEvents(events: List<EventViewModel>)
    fun showLoading()
}