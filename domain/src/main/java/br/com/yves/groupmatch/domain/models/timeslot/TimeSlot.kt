package br.com.yves.groupmatch.domain.models.timeslot

import org.threeten.bp.LocalDateTime

data class TimeSlot(
		val calendarId: Long = 0,
		override val start: LocalDateTime,
		override val end: LocalDateTime,
		override var isBusy: Boolean
): Slot {
	init {
		require(start < end) {
			"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
		}
		require(start.plusHours(1) == end) {
			"Failed to instantiate ${this::class.java.name}. ${this::class.java.name}s must have 1h between start and end"
		}
	}
}

interface Slot {
	val start: LocalDateTime
	val end: LocalDateTime
	var isBusy: Boolean
}