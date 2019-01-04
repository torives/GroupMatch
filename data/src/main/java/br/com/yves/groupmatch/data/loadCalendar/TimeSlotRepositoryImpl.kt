package br.com.yves.groupmatch.data.loadCalendar

import android.content.Context
import androidx.room.Room
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotMapper
import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.TimeSlot
import org.threeten.bp.LocalDateTime

class TimeSlotRepositoryImpl(context: Context) : TimeSlotRepository {

	private val database: RoomDB =
		Room.databaseBuilder(context, RoomDB::class.java, context.getString(R.string.database_name))
			.fallbackToDestructiveMigration() //FIXME: Add Migrations when necessary
			.build()

	override fun insert(timeSlot: TimeSlot) {
		database.timeSlotDAO().insertOrReplace(TimeSlotMapper.from(timeSlot))
	}

	override fun insert(timeSlots: Collection<TimeSlot>) {
		timeSlots.forEach { insert(it) }
	}

	override fun update(timeSlot: TimeSlot) {
		database.timeSlotDAO().update(TimeSlotMapper.from(timeSlot))
	}

	override fun timeSlotsBetween(
		initialDate: LocalDateTime,
		finalDate: LocalDateTime
	): List<TimeSlot> {
		val timeSlots = database.timeSlotDAO()
			.getAllTimeSlotsBetween(initialDate.toString(), finalDate.toString())
		return timeSlots.map { TimeSlotMapper.from(it) }
	}

	override fun delete(timeSlot: TimeSlot) {
		database.timeSlotDAO().delete(TimeSlotMapper.from(timeSlot))
	}
}

