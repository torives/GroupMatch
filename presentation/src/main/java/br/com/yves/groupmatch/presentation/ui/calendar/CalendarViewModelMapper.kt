package br.com.yves.groupmatch.presentation.ui.calendar

import br.com.yves.groupmatch.domain.models.calendar.Calendar

object CalendarViewModelMapper {
	fun map(calendar: Calendar): CalendarViewModel {
		val timeSlots = calendar.timeSlots.map { TimeSlotViewModelMapper.map(it) }
		//FIXME: calcular o número de dias dinamicamente
		return CalendarViewModel(calendar.id, 7, timeSlots)
	}
}

