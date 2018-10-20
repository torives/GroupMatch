package br.com.yves.groupmatch.domain.loadEvents

import br.com.yves.groupmatch.domain.UseCase

class LoadEvents(private val eventsRepository: EventRepository,
                 private val dateRepository: DateRepository
) : UseCase<Unit>() {
    private lateinit var callback: LoadEventsCallback

    override fun execute() {
        val currentWeek = dateRepository.getCurrentWeek()
        val events = eventsRepository.getEventsBetween(currentWeek.start, currentWeek.endInclusive)

        callback.onSuccess(events)
    }

    fun with(callback: LoadEventsCallback): LoadEvents {
        this.callback = callback
        return this
    }
}