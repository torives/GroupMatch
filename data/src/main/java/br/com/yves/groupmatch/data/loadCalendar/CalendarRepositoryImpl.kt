package br.com.yves.groupmatch.data.loadCalendar

import android.content.Context
import androidx.room.Room
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom
import br.com.yves.groupmatch.data.db.week.WeekRoom
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.calendar.CalendarImpl

class CalendarRepositoryImpl(context: Context) : CalendarRepository {
	private val database: RoomDB =
			Room.databaseBuilder(context, RoomDB::class.java, context.getString(R.string.database_name))
					.fallbackToDestructiveMigration() //FIXME: Add Migrations before production launch
					.build()

	//region CalendarRepository
	override fun insert(calendar: Calendar) {
		when(calendar) {
			is CalendarImpl -> {
				val roomCalendar = CalendarMapper.map(calendar)
				database.calendarDAO().insert(roomCalendar)
			}
			is CalendarRoom -> database.calendarDAO().insert(calendar)
			else -> throw IllegalArgumentException(
					"Attempt to store Unknown subclass of ${Calendar::class.java.simpleName} into database"
			)
		}
	}

	override fun getCalendar(week: Week): Calendar? =
		database.calendarDAO().getCalendar(week.start, week.end)


	override fun update(calendar: Calendar) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

