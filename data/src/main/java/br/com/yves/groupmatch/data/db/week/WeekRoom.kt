package br.com.yves.groupmatch.data.db.week

import androidx.room.ColumnInfo
import br.com.yves.groupmatch.domain.models.Week
import org.threeten.bp.LocalDateTime

class WeekRoom(
		start: LocalDateTime,
		@ColumnInfo(name = WeekRoom.COLUMN_END) override var end: LocalDateTime
) : Week(start, end) {
	override var endInclusive = end

	companion object {
		const val COLUMN_START = "start"
		const val COLUMN_END = "end"
	}
}