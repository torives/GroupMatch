package br.com.yves.groupmatch.data.db.timeSlot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.COLUMN_ID
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom.Companion.TABLE_NAME
import org.threeten.bp.LocalDateTime

@Entity(
	tableName = TABLE_NAME,
	indices = [Index(COLUMN_ID, unique = true)]
)
open class TimeSlotRoom(
	@ColumnInfo(name = COLUMN_ID) @PrimaryKey(autoGenerate = true) val id: Int,
	@ColumnInfo(name = COLUMN_DATE) val date: LocalDateTime,
	@ColumnInfo(name = COLUMN_IS_BUSY) val isBusy: Boolean
) {
	companion object {
		const val TABLE_NAME = "time_slot"
		const val COLUMN_ID = "id"
		const val COLUMN_DATE = "date"
		const val COLUMN_IS_BUSY = "is_busy"
	}
}