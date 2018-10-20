package br.com.yves.groupmatch.presentation.ui.loadEvents

interface LoadEventsView {
    fun showEvents(events: List<EventViewModel>)
    fun showLoading()
}