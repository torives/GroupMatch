package com.yves.groupmatch.data

import com.example.domain.Event
import java.time.LocalDateTime

interface EventRepository{
    fun getEventsAt(date: LocalDateTime): List<com.example.domain.Event>
    fun getEventsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<com.example.domain.Event>
}