package br.com.yves.groupmatch.data.db.calendar

import androidx.room.*
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom.Companion.COLUMN_FINAL_DATE
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom.Companion.COLUMN_ID
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom.Companion.COLUMN_INITIAL_DATE
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom.Companion.TABLE_NAME
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import org.threeten.bp.LocalDateTime


@Entity(tableName = TABLE_NAME,
		indices = [Index(value = [COLUMN_ID, COLUMN_INITIAL_DATE, COLUMN_FINAL_DATE], unique = true)]
)
data class CalendarRoom(
		@PrimaryKey(autoGenerate = true)
		@ColumnInfo(name = COLUMN_ID) var id: Long = 0,
		@ColumnInfo(name = COLUMN_OWNER) val owner: String?,
		@ColumnInfo(name = COLUMN_INITIAL_DATE) val initialDate: LocalDateTime,
		@ColumnInfo(name = COLUMN_FINAL_DATE) val finalDate: LocalDateTime,

		@TypeConverters(CalendarSourceConverter::class)
		@ColumnInfo(name = COLUMN_SOURCE) val source: Calendar.Source

) {
	companion object {
		const val TABLE_NAME = "calendar"
		const val COLUMN_ID = "calendarId"
		const val COLUMN_OWNER = "owner"
		const val COLUMN_SOURCE = "source"
		const val COLUMN_INITIAL_DATE = "initial_date"
		const val COLUMN_FINAL_DATE = "final_date"
	}
}