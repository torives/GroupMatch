package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.Calendar
import br.com.yves.groupmatch.domain.CalendarImpl
import br.com.yves.groupmatch.domain.TimeSlot
import br.com.yves.groupmatch.domain.Week


object CalendarFactory {
	fun create(
			owner: String,
			week: Week,
			timeSlots: List<TimeSlot>,
			source: Calendar.Source
	) = CalendarImpl(owner, week, timeSlots, source)
}