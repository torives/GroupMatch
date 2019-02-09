package br.com.yves.groupmatch.domain.loadCalendar

import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.models.calendar.CalendarFactory
import br.com.yves.groupmatch.domain.saveCalendar.SaveCalendar

class LoadCalendar(
		private val dateRepository: DateRepository,
		private val calendarRepository: CalendarRepository,
		private val saveCalendar: SaveCalendar
) : UseCase<Calendar>() {

	override fun execute(): Calendar {
		val currentWeek = dateRepository.getCurrentWeek()
		var calendar = calendarRepository.getCalendar(currentWeek)

		if (calendar == null) {
			//TODO: Define how to identify the user as calendar owner
			calendar = CalendarFactory(dateRepository).create(
					"",
					currentWeek,
					Calendar.Source.LOCAL
			)
			saveCalendar.with(calendar).execute()
		}
		return calendar
	}
}