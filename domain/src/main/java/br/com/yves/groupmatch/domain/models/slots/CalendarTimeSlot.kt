package br.com.yves.groupmatch.domain.models.slots

import org.threeten.bp.LocalDateTime

class CalendarTimeSlot(
		val calendarId: Long = 0,
		start: LocalDateTime,
		end: LocalDateTime,
		isBusy: Boolean
) : TimeSlot(start, end, isBusy) {
	init {
		require(start.plusHours(1) == end) {
			"Failed to instantiate ${this::class.java.name}. ${this::class.java.name}s must have 1h between start and end"
		}
	}
}
