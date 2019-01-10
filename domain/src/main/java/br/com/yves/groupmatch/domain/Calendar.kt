package br.com.yves.groupmatch.domain

interface Calendar {
	val owner: String
	val week: Week
	val timeSlots: List<TimeSlot> //BusyDates
	val source: Source

	enum class Source {
		LOCAL, REMOTE
	}
}

data class CalendarImpl(
		override val owner: String,
		override val week: Week,
		override val timeSlots: List<TimeSlot>,
		override val source: Calendar.Source
): Calendar