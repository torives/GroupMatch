package br.com.yves.groupmatch.domain.models.timeslot

import org.threeten.bp.LocalDateTime

data class TimeSlot(
		val calendarId: Long = 0,
		val start: LocalDateTime,
		val end: LocalDateTime,
		var isBusy: Boolean
) {
	init {
		require(start < end) {
			"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
		}
	}
}