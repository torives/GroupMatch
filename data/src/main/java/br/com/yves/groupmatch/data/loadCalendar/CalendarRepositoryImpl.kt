package br.com.yves.groupmatch.data.loadCalendar

import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.calendar.CalendarRoomMapper
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoomMapper
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

class CalendarRepositoryImpl : CalendarRepository {
	private val database
		get() = RoomDB.getInstance()

	//region CalendarRepository
	override fun insert(calendar: Calendar) {
		require(calendar.timeSlots.isNotEmpty()) {
			"Attempt to insert calendar with no ${TimeSlot::class.java.simpleName}s into database"
		}

		val roomCalendar = CalendarRoomMapper.map(calendar)
		val calendarId = database.calendarDAO().insert(roomCalendar)

		for (timeSlot in calendar.timeSlots) {
			val roomTimeSlot = TimeSlotRoomMapper.map(timeSlot, calendarId)
			database.timeSlotDAO().insert(roomTimeSlot)
		}
	}

	override fun getCalendar(week: Week): Calendar? {
		return database.calendarDAO().getCalendar(week.start, week.end)?.let { calendar ->
			database.timeSlotDAO().getTimeSlotsFromCalendar(calendar.id)?.let { timeSlots ->
				CalendarRoomMapper.map(calendar, timeSlots)
			}
		}
	}

	override fun getCalendar(id: Long): Calendar? {
		return database.calendarDAO().getCalendar(id)?.let { calendar ->
			database.timeSlotDAO().getTimeSlotsFromCalendar(calendar.id)?.let { timeSlots ->
				CalendarRoomMapper.map(calendar, timeSlots)
			}
		}
	}

	override fun update(calendar: Calendar) {
		val calendarRoom = CalendarRoomMapper.map(calendar)
		database.calendarDAO().update(calendarRoom)

		val timeSlots = calendar.timeSlots.map { TimeSlotRoomMapper.map(it) }
		timeSlots.forEach {
			database.timeSlotDAO().update(it)
		}
	}

	override fun delete(calendar: Calendar) {
		database.calendarDAO().delete(calendar.id)
	}
//endregion
}


