package br.com.yves.groupmatch.presentation.ui.calendar

import br.com.yves.groupmatch.domain.loadCalendar.LoadCalendar
import br.com.yves.groupmatch.domain.updateCalendar.UpdateCalendar

class CalendarPresenter(
		private val view: CalendarView,
		private val loadCalendar: LoadCalendar,
		private val updateCalendar: UpdateCalendar
) {
	fun onViewCreated() = showCalendar()

	private fun showCalendar() {
		val calendar = loadCalendar.execute()
		val calendarViewModel = CalendarViewModelMapper.map(calendar)

		view.showCalendar(calendarViewModel)
	}

	fun onTimeSlotClicked(calendarId: Long, timeSlot: TimeSlotViewModel) {
		val slot = TimeSlotViewModelMapper.map(calendarId, timeSlot)
		updateCalendar.with(slot).execute()
		showCalendar()
	}
}