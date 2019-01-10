package br.com.yves.groupmatch.data.db.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom.Companion.COLUMN_WEEK
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom.Companion.TABLE_NAME
import br.com.yves.groupmatch.domain.Calendar
import br.com.yves.groupmatch.domain.TimeSlot
import br.com.yves.groupmatch.domain.Week

@Entity(
		tableName = TABLE_NAME,
		indices = [Index(COLUMN_WEEK, unique = true)]
)
class CalendarRoom(
		@ColumnInfo(name = COLUMN_WEEK) override val week: Week,
		@ColumnInfo(name = COLUMN_OWNER) override val owner: String,
		@ColumnInfo(name = COLUMN_SOURCE) override val timeSlots: List<TimeSlot>,
		@ColumnInfo(name = COLUMN_TIMESLOTS) override val source: Calendar.Source
) : Calendar {
	companion object {
		const val TABLE_NAME = "calendar"
		const val COLUMN_WEEK = "week"
		const val COLUMN_OWNER = "owner"
		const val COLUMN_SOURCE = "source"
		const val COLUMN_TIMESLOTS = "timeslots"
	}
}