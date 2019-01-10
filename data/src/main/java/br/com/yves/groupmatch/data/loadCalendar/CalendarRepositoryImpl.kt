package br.com.yves.groupmatch.data.loadCalendar

import android.content.Context
import androidx.room.Room
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotMapper
import br.com.yves.groupmatch.domain.Calendar
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.TimeSlot
import br.com.yves.groupmatch.domain.Week
import org.threeten.bp.LocalDateTime

class CalendarRepositoryImpl(context: Context) : CalendarRepository {
	private val database: RoomDB =
		Room.databaseBuilder(context, RoomDB::class.java, context.getString(R.string.database_name))
			.fallbackToDestructiveMigration() //FIXME: Add Migrations when necessary
			.build()

	//region CalendarRepository
	override fun add(calendar: Calendar) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getCalendar(week: Week): Calendar? {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun update(week: Week, slots: Collection<TimeSlot>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun delete(calendar: Calendar) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion
}

