package br.com.yves.groupmatch.data.db.week

import br.com.yves.groupmatch.domain.models.Week
import org.threeten.bp.LocalDateTime

data class WeekRoom(
		override val start: LocalDateTime,
		override val end: LocalDateTime
) : Week(start, end) {
	companion object {
		const val COLUMN_START = "start"
		const val COLUMN_END = "end"
	}
}