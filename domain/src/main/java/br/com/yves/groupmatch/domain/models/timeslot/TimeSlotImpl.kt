package br.com.yves.groupmatch.domain.models.timeslot

import org.threeten.bp.LocalDateTime

data class TimeSlotImpl(
		override val start: LocalDateTime,
		override val end: LocalDateTime,
		override var isBusy: Boolean
) : TimeSlot {
	init {
		require(start < end) {
			"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
		}
	}
}