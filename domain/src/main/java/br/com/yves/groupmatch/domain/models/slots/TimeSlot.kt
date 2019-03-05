package br.com.yves.groupmatch.domain.models.slots

import org.threeten.bp.LocalDateTime

open class TimeSlot(
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