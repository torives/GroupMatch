package br.com.yves.groupmatch.data.db.timeSlot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_CALENDAR_ID
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_END
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_START
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.TABLE_NAME
import org.threeten.bp.LocalDateTime

@Entity(
		tableName = TABLE_NAME,
		primaryKeys = [
			COLUMN_CALENDAR_ID,
			COLUMN_START,
			COLUMN_END
		],
		foreignKeys = [
			ForeignKey(
					entity = CalendarRoom::class,
					parentColumns = arrayOf(CalendarRoom.COLUMN_ID),
					childColumns = arrayOf(COLUMN_CALENDAR_ID),
					onDelete = CASCADE
			)
		],
		indices = [
			Index(COLUMN_START, COLUMN_END, unique = true),
			Index(TimeSlotRoom.COLUMN_CALENDAR_ID)
		]
)
data class TimeSlotRoom(
		@ColumnInfo(name = COLUMN_CALENDAR_ID) val calendarId: Long,
		@ColumnInfo(name = COLUMN_START) val start: LocalDateTime,
		@ColumnInfo(name = COLUMN_END) val end: LocalDateTime,
		@ColumnInfo(name = COLUMN_IS_BUSY) val isBusy: Boolean
) {
	init {
		require(start < end) {
			"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
		}
	}

	companion object {
		const val TABLE_NAME = "time_slot"
		const val COLUMN_CALENDAR_ID = "calendar_id"
		const val COLUMN_START = "start"
		const val COLUMN_END = "end"
		const val COLUMN_IS_BUSY = "is_busy"
	}
}