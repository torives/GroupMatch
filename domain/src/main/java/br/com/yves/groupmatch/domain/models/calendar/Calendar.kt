package br.com.yves.groupmatch.domain.models.calendar

import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.slots.CalendarTimeSlot

data class Calendar(
		val id: Long = 0,
		val owner: String,
		val week: Week,
		val calendarTimeSlots: MutableList<CalendarTimeSlot>,
		val source: Source
) {
	enum class Source {
		LOCAL, REMOTE
	}
}