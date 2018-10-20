package br.com.yves.groupmatch.domain.loadEvents

interface LoadEventsCallback {
    fun onSuccess(events: List<Event>)
    fun onFailure()
}