package br.com.yves.groupmatch.domain.loadCalendar

import br.com.yves.groupmatch.domain.Calendar
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar
import br.com.yves.groupmatch.domain.saveCalendar.SaveCalendar

class LoadCalendar(
		private val dateRepository: DateRepository,
		private val calendarRepository: CalendarRepository,
		private val createCalendar: CreateCalendar,
		private val saveCalendar: SaveCalendar
) : UseCase<Calendar>() {

	override fun execute(): Calendar {
		val currentWeek = dateRepository.getCurrentWeek()
		var calendar = calendarRepository.getCalendar(currentWeek)

		if (calendar == null) {
			//TODO: Define how to identify the user as calendar owner
			calendar = createCalendar.with(currentWeek, "").execute()
			saveCalendar.with(calendar).execute()
		}
		return calendar
	}
}

