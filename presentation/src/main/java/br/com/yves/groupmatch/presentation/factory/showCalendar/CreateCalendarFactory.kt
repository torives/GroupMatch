package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar

object CreateCalendarFactory {
	fun create(dateRepository: DateRepository, timeSlotRepository: TimeSlotRepository) =
		CreateCalendar(dateRepository, timeSlotRepository)
}
