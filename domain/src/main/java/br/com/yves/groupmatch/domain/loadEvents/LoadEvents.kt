package br.com.yves.groupmatch.domain.loadEvents

import br.com.yves.groupmatch.domain.UseCase

class LoadEvents(private val eventsRepository: EventRepository,
                 private val dateRepository: DateRepository
) : UseCase<List<Event>>() {

    override fun execute(): List<Event> {
        val currentWeek = dateRepository.getCurrentWeek()

        return eventsRepository.getEventsBetween(
                currentWeek.start,
                currentWeek.endInclusive
        )
    }
}