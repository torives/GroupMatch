package br.com.yves.groupmatch.domain.showCalendar

import org.threeten.bp.LocalDateTime

interface TimeSlotRepository {
	fun getTimeSlotsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<TimeSlot>
}