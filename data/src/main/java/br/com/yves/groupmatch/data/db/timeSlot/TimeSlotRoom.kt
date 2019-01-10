package br.com.yves.groupmatch.data.db.timeSlot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_END
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_START
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.TABLE_NAME
import br.com.yves.groupmatch.domain.TimeSlot
import org.threeten.bp.LocalDateTime

@Entity(
		tableName = TABLE_NAME,
		indices = [Index(COLUMN_START, COLUMN_END, unique = true)]
)
open class TimeSlotRoom(
		@ColumnInfo(name = COLUMN_START) @PrimaryKey override var start: LocalDateTime,
		@ColumnInfo(name = COLUMN_END) @PrimaryKey override var end: LocalDateTime,
		@ColumnInfo(name = COLUMN_IS_BUSY) override var isBusy: Boolean
) : TimeSlot(start, end, isBusy) {

	companion object {
		const val TABLE_NAME = "time_slot"
		const val COLUMN_START = "start"
		const val COLUMN_END = "end"
		const val COLUMN_IS_BUSY = "is_busy"
	}
}