package br.com.yves.groupmatch.domain.loadEvents

import org.threeten.bp.LocalDateTime

interface EventRepository{
    fun getEventsAt(date: LocalDateTime): List<Event>
    fun getEventsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<Event>
}