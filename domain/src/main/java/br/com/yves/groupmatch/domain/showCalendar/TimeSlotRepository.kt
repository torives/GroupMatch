package br.com.yves.groupmatch.domain.showCalendar

import org.threeten.bp.LocalDateTime

interface TimeSlotRepository {
	fun deleteTimeSlot(timeSlot: TimeSlot)
	fun getTimeSlotsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<TimeSlot>
}