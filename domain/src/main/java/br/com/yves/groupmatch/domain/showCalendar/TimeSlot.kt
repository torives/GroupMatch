package br.com.yves.groupmatch.domain.showCalendar

import org.threeten.bp.LocalDateTime

data class TimeSlot(
	val date: LocalDateTime,
	val isBusy: Boolean
)