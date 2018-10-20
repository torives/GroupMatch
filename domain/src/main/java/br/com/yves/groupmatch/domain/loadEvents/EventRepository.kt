package br.com.yves.groupmatch.domain.loadEvents

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface EventRepository{
    fun getEventsAt(date: LocalDate): List<Event>
    fun getEventsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<Event>
}