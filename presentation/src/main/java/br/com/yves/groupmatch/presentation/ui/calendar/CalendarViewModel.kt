package br.com.yves.groupmatch.presentation.ui.calendar

data class CalendarViewModel(
		val id: Long,
		val daysCount: Int,
		val timeSlots: List<TimeSlotViewModel>
)