package br.com.yves.groupmatch.data

import br.com.yves.groupmatch.domain.loadEvents.Event

interface GetEventsFromCurrentWeekCallback {
    fun onGetEventsFromCurrentWeekSuccess(events: List<Event>)
}