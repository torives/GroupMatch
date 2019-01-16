package br.com.yves.groupmatch.domain.models.calendar

import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

data class CalendarImpl(
		override val owner: String,
		override val week: Week,
		override val timeSlots: List<TimeSlot>,
		override val source: Calendar.Source
) : Calendar