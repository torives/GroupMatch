package br.com.yves.groupmatch.data.loadCalendar

import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.calendar.CalendarMapper
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotMapper
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

		val roomCalendar = CalendarMapper.map(calendar)
		val calendarId = database.calendarDAO().insert(roomCalendar)

		for (timeSlot in calendar.timeSlots) {
			val roomTimeSlot = TimeSlotMapper.from(timeSlot, calendarId)
			database.timeSlotDAO().insert(roomTimeSlot)
		}
	}


	override fun getCalendar(week: Week): Calendar? {
		return database.calendarDAO().getCalendar(week.start, week.end)?.apply {
			val timeSlots = database.timeSlotDAO().getAllTimeSlotsFromCalendar(id)
			this.timeSlots = timeSlots ?: listOf()
		}
	}

	override fun getCalendar(id: Long): Calendar? {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}


	override fun update(calendar: Calendar) {
		database.calendarDAO().getCalendar(calendar.week.start, calendar.week.end)?.apply {
			timeSlots = calendar.timeSlots
		}?.let {
			database.calendarDAO().update(it)
		}
	}

	override fun delete(calendar: Calendar) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
//endregion
}


