package br.com.yves.groupmatch.domain.loadCalendar

import org.threeten.bp.LocalDateTime

data class TimeSlot(
	val date: LocalDateTime,
	var isBusy: Boolean
)