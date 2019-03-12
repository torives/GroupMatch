package br.com.yves.groupmatch.domain.models.slots

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import java.io.Serializable

open class TimeSlot(
		val start: LocalDateTime,
		val end: LocalDateTime,
		var isBusy: Boolean
) : Serializable {
	val duration: Duration = Duration.between(start, end)

	init {
		require(start < end) {
			"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
		}
	}
}