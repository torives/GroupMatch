package br.com.yves.groupmatch.presentation.ui.loadEvents

interface CalendarView {
    fun showEvents(events: List<EventViewModel>)
    fun showLoading()
}