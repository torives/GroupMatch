package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.domain.createCalendar.CreateCalendarFactory
import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.saveCalendar.SaveCalendarFactory
import br.com.yves.groupmatch.domain.loadCalendar.LoadCalendar
import br.com.yves.groupmatch.presentation.factory.DateRepositoryFactory

object LoadCalendarFactory {

	fun create(timeSlotRepository: TimeSlotRepository): LoadCalendar {
		val dateRepository = DateRepositoryFactory.create()

		return LoadCalendar(
			dateRepository,
			timeSlotRepository,
			CreateCalendarFactory.create(dateRepository),
			SaveCalendarFactory.create(timeSlotRepository)
		)
	}
}