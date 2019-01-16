package br.com.yves.groupmatch.data.db.calendar

import androidx.room.*
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom.Companion.TABLE_NAME
import br.com.yves.groupmatch.data.db.week.WeekRoom
import br.com.yves.groupmatch.data.db.week.WeekRoom.Companion.COLUMN_END
import br.com.yves.groupmatch.data.db.week.WeekRoom.Companion.COLUMN_START
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

@Entity(tableName = TABLE_NAME,
		indices = [Index(COLUMN_START, COLUMN_END, unique = true)]
)
data class CalendarRoom(
		@Embedded override val week: WeekRoom,
		@ColumnInfo(name = COLUMN_OWNER) override val owner: String,
		@ColumnInfo(name = COLUMN_SOURCE) override val timeSlots: List<TimeSlot>,
		@ColumnInfo(name = COLUMN_TIMESLOTS) override val source: Calendar.Source
) : Calendar {
	@PrimaryKey(autoGenerate = true) val id: Int = 0

	companion object {
		const val TABLE_NAME = "calendar"
		const val COLUMN_ID = "id"
		const val COLUMN_OWNER = "owner"
		const val COLUMN_SOURCE = "source"
		const val COLUMN_TIMESLOTS = "timeslots"
	}
}