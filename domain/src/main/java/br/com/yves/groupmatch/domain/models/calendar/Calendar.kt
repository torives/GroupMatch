package br.com.yves.groupmatch.domain.models.calendar

import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

interface Calendar {
	val owner: String
	val week: Week
	val timeSlots: List<TimeSlot>
	val source: Source

	enum class Source {
		LOCAL, REMOTE
	}
}