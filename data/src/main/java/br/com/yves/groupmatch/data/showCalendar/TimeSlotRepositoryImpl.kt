package br.com.yves.groupmatch.data.showCalendar

import android.content.Context
import androidx.room.Room
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotMapper
import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import br.com.yves.groupmatch.domain.showCalendar.TimeSlotRepository
import org.threeten.bp.LocalDateTime

class TimeSlotRepositoryImpl(context: Context) : TimeSlotRepository {
	private val database: RoomDB =
		Room.databaseBuilder(context, RoomDB::class.java, context.getString(R.string.database_name))
			.fallbackToDestructiveMigration() //FIXME: Add Migrations when necessary
			.build()

	override fun getTimeSlotsBetween(
		initialDate: LocalDateTime,
		finalDate: LocalDateTime
	): List<TimeSlot> {
		val timeSlots = database.timeSlotDAO()
			.getAllTimeSlotsBetween(initialDate.toString(), finalDate.toString())
		return timeSlots.map { TimeSlotMapper.from(it) }
	}
}

