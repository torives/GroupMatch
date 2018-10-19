package br.com.yves.groupmatch.domain.loadEvents

import br.com.yves.groupmatch.domain.UseCase

class LoadEvents(private val eventsRepository: EventRepository,
                 private val dateRepository: DateRepository
) : UseCase<List<Event>>() {
    private lateinit var callback: LoadEventsCallback

    override fun execute(): List<Event> {
        return listOf()
    }

    fun with(callback: LoadEventsCallback): LoadEvents {
        this.callback = callback
        return this
    }
}

interface LoadEventsCallback {
    fun onSuccess(events: List<Event>)
    fun onFailure()
}