package br.com.yves.groupmatch.data.db.timeSlot

import br.com.yves.groupmatch.domain.models.slots.CalendarTimeSlot

object CalendarTimeSlotRoomMapper {
	fun map(calendarTimeSlot: CalendarTimeSlot, calendarId: Long? = null) = CalendarTimeSlotRoom(
			calendarId ?: calendarTimeSlot.calendarId,
			calendarTimeSlot.start,
			calendarTimeSlot.end,
			calendarTimeSlot.isBusy
	)

	fun map(calendarTimeSlot: CalendarTimeSlotRoom) = CalendarTimeSlot(
			calendarTimeSlot.calendarId,
			calendarTimeSlot.start,
			calendarTimeSlot.end,
			calendarTimeSlot.isBusy
	)
}