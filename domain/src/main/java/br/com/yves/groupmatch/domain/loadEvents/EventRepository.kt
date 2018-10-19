package br.com.yves.groupmatch.domain.loadEvents

import java.time.LocalDateTime

interface EventRepository{
    fun getEventsAt(date: LocalDateTime): List<Event>
    fun getEventsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<Event>
}