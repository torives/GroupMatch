package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.Week
import org.threeten.bp.LocalDateTime

data class ClientCalendar(
		val owner: String,
		private val weekStart: LocalDateTime,
		private val weekEnd: LocalDateTime,
		val busyDates: List<LocalDateTime> //BusyDates
) {
	val week: Week
		get() = weekStart.rangeTo(weekEnd)
}