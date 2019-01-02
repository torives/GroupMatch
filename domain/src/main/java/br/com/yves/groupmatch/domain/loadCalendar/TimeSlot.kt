package br.com.yves.groupmatch.domain.loadCalendar

import org.threeten.bp.LocalDateTime

data class TimeSlot(
	val date: LocalDateTime,
	var isBusy: Boolean
) {
	fun merge(timeSlot: TimeSlot) {
		if(timeSlot.isBusy) {
			this.isBusy = true
		}
	}
}