package br.com.yves.groupmatch.data.db.timeSlot

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_CALENDAR_ID
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_END
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_ID
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_START
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.TABLE_NAME
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import org.threeten.bp.LocalDateTime

@Entity(
		tableName = TABLE_NAME,
		indices = [
			Index(COLUMN_ID, unique = true),
			Index(COLUMN_START, COLUMN_END, unique = true),
			Index(COLUMN_CALENDAR_ID)
		],
		foreignKeys = [
			ForeignKey(
					entity = CalendarRoom::class,
					parentColumns = arrayOf(CalendarRoom.COLUMN_ID),
					childColumns = arrayOf(COLUMN_CALENDAR_ID),
					onDelete = CASCADE
			)
		]
)
class TimeSlotRoom(
		@ColumnInfo(name = COLUMN_CALENDAR_ID) val calendarId: Long,
		override val start: LocalDateTime,
		override val end: LocalDateTime,
		@ColumnInfo(name = COLUMN_IS_BUSY) override val isBusy: Boolean
) : TimeSlot {
	@PrimaryKey(autoGenerate = true) @ColumnInfo(name = COLUMN_ID) var id = 0L

	init {
		require(start < end) {
			"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
		}
	}

	companion object {
		const val TABLE_NAME = "time_slot"
		const val COLUMN_ID = "id"
		const val COLUMN_CALENDAR_ID = "calendar_id"
		const val COLUMN_START = "start"
		const val COLUMN_END = "end"
		const val COLUMN_IS_BUSY = "is_busy"
	}
}