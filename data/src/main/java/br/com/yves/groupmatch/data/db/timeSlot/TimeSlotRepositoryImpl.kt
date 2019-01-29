package br.com.yves.groupmatch.data.db.timeSlot

import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import org.threeten.bp.LocalDateTime

class TimeSlotRepositoryImpl: TimeSlotRepository {
	private val database
		get() = RoomDB.getInstance()

	override fun getTimeSlot(start: LocalDateTime, end: LocalDateTime): TimeSlot? {
		database.timeSlotDAO().
	}

	override fun update(timeSlot: TimeSlot) {
		database.timeSlotDAO().update(timeSlot as TimeSlotRoom)
	}

}