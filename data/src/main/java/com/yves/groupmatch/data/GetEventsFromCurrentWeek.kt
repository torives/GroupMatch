package com.yves.groupmatch.data

import com.example.domain.Event
import com.example.domain.UseCase

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