package br.com.yves.groupmatch.domain.sendCalendar

import org.threeten.bp.LocalDateTime

data class ClientCalendar(
		val owner: String,
		val weekEnd: LocalDateTime,
		val weekStart: LocalDateTime,
		val busyDates: List<LocalDateTime> //BusyDates
)