package br.com.yves.groupmatch.data.db.timeSlot

import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

object TimeSlotRoomMapper {
	fun map(timeSlot: TimeSlot, calendarId: Long? = null) = TimeSlotRoom(
			calendarId ?: timeSlot.calendarId,
			timeSlot.start,
			timeSlot.end,
			timeSlot.isBusy
	)

	fun map(timeSlot: TimeSlotRoom) = TimeSlot(
			timeSlot.calendarId,
			timeSlot.start,
			timeSlot.end,
			timeSlot.isBusy
	)
}