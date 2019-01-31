package br.com.yves.groupmatch.domain.models.calendar

import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

data class Calendar(
		val id: Long = 0,
		val owner: String,
		val week: Week,
		val timeSlots: List<TimeSlot>,
		val source: Source
) {
	enum class Source {
		LOCAL, REMOTE
	}
}