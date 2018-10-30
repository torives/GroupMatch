package br.com.yves.groupmatch.domain.loadEvents

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.showCalendar.Week


class LoadEvents(private val eventsRepository: EventRepository) : UseCase<List<Event>>() {

    private lateinit var week: Week

    fun from(week: Week): LoadEvents {
        this.week = week
        return this
    }

    override fun execute(): List<Event> {
        return eventsRepository.getEventsBetween(
            week.start,
            week.endInclusive
        )
    }
}