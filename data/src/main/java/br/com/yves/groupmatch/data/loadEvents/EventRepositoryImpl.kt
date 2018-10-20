package br.com.yves.groupmatch.data.loadEvents

import br.com.yves.groupmatch.domain.loadEvents.Event
import br.com.yves.groupmatch.domain.loadEvents.EventRepository
import org.threeten.bp.LocalDateTime

class EventRepositoryImpl : EventRepository {
    override fun getEventsAt(date: LocalDateTime): List<Event> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEventsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<Event> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}