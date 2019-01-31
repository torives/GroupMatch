package br.com.yves.groupmatch.data.db.calendar

import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

object CalendarMapper {
	fun map(calendar: Calendar): CalendarRoom =
			CalendarRoom(
					calendar.id,
					calendar.week.start,
					calendar.week.end,
					calendar.owner,
					calendar.source
			)

	fun map(calendar: CalendarRoom, timeSlots: List<TimeSlot>): Calendar =
			Calendar(
					calendar.id,
					calendar.owner,
					Week(calendar.initialDate, calendar.finalDate),
					timeSlots,
					calendar.source
			)
}