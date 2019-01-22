package br.com.yves.groupmatch.data.db.calendar

import androidx.room.*
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom.Companion.TABLE_NAME
import br.com.yves.groupmatch.data.db.week.WeekRoom
import br.com.yves.groupmatch.data.db.week.WeekRoom.Companion.COLUMN_END
import br.com.yves.groupmatch.data.db.week.WeekRoom.Companion.COLUMN_START
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot


@Entity(tableName = TABLE_NAME,
		indices = [Index(value = [COLUMN_START, COLUMN_END], unique = true)]
)
data class CalendarRoom(
		@Embedded override val week: WeekRoom,
		override val owner: String,
		@TypeConverters(SourceConverter::class) override val source: Calendar.Source
) : Calendar {
	@PrimaryKey(autoGenerate = true)
	var id = 0L

	@Ignore
	@Embedded
	override var timeSlots: List<TimeSlot> = listOf()

	companion object {
		const val TABLE_NAME = "calendar"
		const val COLUMN_ID = "id"
		const val COLUMN_OWNER = "owner"
		const val COLUMN_SOURCE = "source"
	}
}

class SourceConverter {
	@TypeConverter
	fun toSource(source: Int): Calendar.Source {
		return when (source) {
			Calendar.Source.LOCAL.ordinal -> Calendar.Source.LOCAL
			Calendar.Source.REMOTE.ordinal -> Calendar.Source.REMOTE
			else -> throw IllegalArgumentException("Could not recognize source")
		}
	}

	@TypeConverter
	fun toInteger(status: Calendar.Source): Int {
		return status.ordinal
	}
}