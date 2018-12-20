package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import org.threeten.bp.LocalDateTime

data class BusyCalendar(
		val weekStart: LocalDateTime,
		val weekEnd: LocalDateTime,
		val busySlots: List<TimeSlot>
)