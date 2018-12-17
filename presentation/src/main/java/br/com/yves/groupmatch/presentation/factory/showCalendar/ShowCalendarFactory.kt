package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.domain.createCalendar.CreateCalendarFactory
import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.saveCalendar.SaveCalendarFactory
import br.com.yves.groupmatch.domain.showCalendar.ShowCalendar
import br.com.yves.groupmatch.presentation.factory.DateRepositoryFactory

object ShowCalendarFactory {

	fun create(timeSlotRepository: TimeSlotRepository): ShowCalendar {
		val dateRepository = DateRepositoryFactory.create()

		return ShowCalendar(
			dateRepository,
			timeSlotRepository,
			CreateCalendarFactory.create(dateRepository),
			SaveCalendarFactory.create(timeSlotRepository)
		)
	}
}