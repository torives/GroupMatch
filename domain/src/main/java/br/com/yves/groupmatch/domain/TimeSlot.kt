package br.com.yves.groupmatch.domain

import org.threeten.bp.LocalDateTime


open class TimeSlot(
		open val start: LocalDateTime,
		open val end: LocalDateTime,
		open var isBusy: Boolean
) {
	init {
		require(start < end) {
			"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
		}
	}

	fun canMerge(other: TimeSlot): Boolean {
		return other.end == this.start || other.start == this.end
	}

	fun merge(other: TimeSlot): TimeSlot = this
}
