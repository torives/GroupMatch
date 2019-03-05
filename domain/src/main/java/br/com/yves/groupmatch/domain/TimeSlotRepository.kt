package br.com.yves.groupmatch.domain

import br.com.yves.groupmatch.domain.models.slots.CalendarTimeSlot
import org.threeten.bp.LocalDateTime

interface TimeSlotRepository {
	fun getTimeSlot(start: LocalDateTime, end: LocalDateTime) : CalendarTimeSlot?
	fun update(calendarTimeSlot: CalendarTimeSlot)
}

