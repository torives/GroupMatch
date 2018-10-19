package br.com.yves.groupmatch.data

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.loadEvents.Event
import br.com.yves.groupmatch.domain.loadEvents.DateRepository
import br.com.yves.groupmatch.domain.loadEvents.EventRepository

class GetEventsFromCurrentWeek(private val eventRepository: EventRepository,
                               private val dateRepository: DateRepository
) : UseCase<Unit>() {

    private lateinit var callback: GetEventsFromCurrentWeekCallback

    //TODO:
    override fun execute() {
        callback.onGetEventsFromCurrentWeekSuccess(listOf(Event()))
    }

    fun with(callback: GetEventsFromCurrentWeekCallback): GetEventsFromCurrentWeek {
        this.callback = callback
        return this
    }
}