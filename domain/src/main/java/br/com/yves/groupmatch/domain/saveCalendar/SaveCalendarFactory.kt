package br.com.yves.groupmatch.domain.saveCalendar

import br.com.yves.groupmatch.domain.TimeSlotRepository

object SaveCalendarFactory {
	fun create(timeSlotRepository: TimeSlotRepository) = SaveCalendar(timeSlotRepository)
}