package br.com.yves.groupmatch.domain.models.slots

import org.threeten.bp.LocalDateTime

class TimeSlotBuilder {
	var start: LocalDateTime? = null
		private set
	var end: LocalDateTime? = null
		private set
	var isEmpty = true

	fun setStart(start: LocalDateTime) = apply {
		this.start = start
		isEmpty = false
	}

	fun setEnd(end: LocalDateTime) = apply {
		this.end = end
		isEmpty = false
	}

	fun build() = TimeSlot(start!!, end!!, false)

	fun clear() {
		start = null
		end = null
		isEmpty = true
	}
}