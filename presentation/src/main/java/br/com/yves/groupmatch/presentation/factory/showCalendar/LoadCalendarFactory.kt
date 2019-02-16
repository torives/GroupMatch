package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.data.UserRepo
import br.com.yves.groupmatch.data.loadCalendar.DateRepositoryFactory
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.loadCalendar.LoadCalendar
import br.com.yves.groupmatch.domain.saveCalendar.SaveCalendarFactory
import br.com.yves.groupmatch.presentation.GroupMatchApplication

object LoadCalendarFactory {

	fun create(calendarRepository: CalendarRepository): LoadCalendar {
		val dateRepository = DateRepositoryFactory.create()

		return LoadCalendar(
				dateRepository,
				calendarRepository,
				UserRepo(GroupMatchApplication.instance),
				SaveCalendarFactory.create(calendarRepository)
		)
	}
}