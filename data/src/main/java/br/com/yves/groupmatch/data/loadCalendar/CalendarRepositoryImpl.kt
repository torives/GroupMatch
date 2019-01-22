package br.com.yves.groupmatch.data.loadCalendar

import android.content.Context
import androidx.room.Room
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotMapper
import br.com.yves.groupmatch.data.db.week.WeekRoom
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.calendar.CalendarImpl
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlotImpl

class CalendarRepositoryImpl(context: Context) : CalendarRepository {
	private val database: RoomDB =
			Room.databaseBuilder(context, RoomDB::class.java, context.getString(R.string.database_name))
					.fallbackToDestructiveMigration() //FIXME: Add Migrations before production launch
					.build()

	//region CalendarRepository
	override fun insert(calendar: Calendar) {
		require(calendar.timeSlots.isNotEmpty()) {
			"Attempt to insert calendar with no ${TimeSlot::class.java.simpleName}s into database"
		}

		val roomCalendar = CalendarMapper.map(calendar)
		val calendarId = database.calendarDAO().insert(roomCalendar)

		for (timeSlot in calendar.timeSlots) {
			(timeSlot as TimeSlotImpl).let {
				val roomTimeSlot = TimeSlotMapper.from(timeSlot, calendarId)
				database.timeSlotDAO().insertOrReplace(roomTimeSlot)
			}
		}
	}


	override fun getCalendar(week: Week): Calendar? =
			database.calendarDAO().getCalendar(week.start, week.end)


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


object CalendarMapper {
	fun map(calendar: Calendar): CalendarRoom =
			CalendarRoom(
					WeekRoom(calendar.week.start, calendar.week.end),
					calendar.owner,
					calendar.source
			).apply { timeSlots = calendar.timeSlots }

	fun map(calendar: CalendarRoom): Calendar = CalendarImpl(
			calendar.owner,
			calendar.week,
			calendar.timeSlots,
			calendar.source
	)
}

