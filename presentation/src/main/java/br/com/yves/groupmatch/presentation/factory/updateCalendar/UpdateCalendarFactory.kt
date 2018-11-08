package br.com.yves.groupmatch.presentation.factory.updateCalendar

import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.updateCalendar.UpdateCalendar

object UpdateCalendarFactory {
	fun create(timeSlotRepository: TimeSlotRepository) = UpdateCalendar(timeSlotRepository)
}
