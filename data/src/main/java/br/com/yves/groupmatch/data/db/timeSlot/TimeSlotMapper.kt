package br.com.yves.groupmatch.data.db.timeSlot

import br.com.yves.groupmatch.domain.showCalendar.TimeSlot

object TimeSlotMapper {
	fun from(timeSlot: TimeSlotRoom) = TimeSlot(timeSlot.date, timeSlot.isBusy)
}