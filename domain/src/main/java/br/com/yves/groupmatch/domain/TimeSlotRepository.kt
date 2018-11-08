package br.com.yves.groupmatch.domain

import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import org.threeten.bp.LocalDateTime

interface TimeSlotRepository {
	fun insert(timeSlot: TimeSlot)
	fun insert(timeSlots: Collection<TimeSlot>)
	fun update(timeSlot: TimeSlot)
	fun delete(timeSlot: TimeSlot)
	fun timeSlotsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<TimeSlot>
}