package br.com.yves.groupmatch.data.db.calendar

import br.com.yves.groupmatch.data.db.timeSlot.CalendarTimeSlotRoom
import br.com.yves.groupmatch.data.db.timeSlot.CalendarTimeSlotRoomMapper
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.calendar.Calendar

object CalendarRoomMapper {
	fun map(calendar: Calendar): CalendarRoom =
			CalendarRoom(
					calendar.id,
					calendar.owner,
					calendar.week.start,
					calendar.week.end,
					calendar.source
			)

	fun map(calendar: CalendarRoom, calendarTimeSlots: List<CalendarTimeSlotRoom>): Calendar {
		val mappedTimeSlots = calendarTimeSlots.map { CalendarTimeSlotRoomMapper.map(it) }
		return Calendar(
				calendar.id,
				calendar.owner,
				Week(calendar.initialDate, calendar.finalDate),
				mappedTimeSlots.toMutableList(),
				calendar.source
		)
	}
}