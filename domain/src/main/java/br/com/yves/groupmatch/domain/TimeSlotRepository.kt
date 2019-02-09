package br.com.yves.groupmatch.domain

import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import org.threeten.bp.LocalDateTime

interface TimeSlotRepository {
	fun getTimeSlot(start: LocalDateTime, end: LocalDateTime) : TimeSlot?
	fun update(timeSlot: TimeSlot)
}

