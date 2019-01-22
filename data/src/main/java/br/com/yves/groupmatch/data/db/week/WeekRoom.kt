package br.com.yves.groupmatch.data.db.week

import androidx.room.ColumnInfo
import androidx.room.Ignore
import br.com.yves.groupmatch.domain.models.Week
import org.threeten.bp.LocalDateTime

class WeekRoom(
		@ColumnInfo(name = WeekRoom.COLUMN_START) override var start: LocalDateTime,
		@ColumnInfo(name = WeekRoom.COLUMN_END) override var end: LocalDateTime
) : Week(start, end) {
	@Ignore override var endInclusive = end

	companion object {
		const val COLUMN_START = "week_start"
		const val COLUMN_END = "week_end"
	}
}