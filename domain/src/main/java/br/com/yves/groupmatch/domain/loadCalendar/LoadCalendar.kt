package br.com.yves.groupmatch.domain.loadCalendar

import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.models.calendar.CalendarFactory
import br.com.yves.groupmatch.domain.saveCalendar.SaveCalendar

class LoadCalendar(
		private val dateRepository: DateRepository,
		private val calendarRepository: CalendarRepository,
		private val authenticationService: AuthenticationService,
		private val saveCalendar: SaveCalendar
) : UseCase<Calendar>() {

	override fun execute(): Calendar {
		val currentWeek = dateRepository.getCurrentWeek()
		var calendar = calendarRepository.getCalendar(currentWeek)

		if (calendar == null) {
			val owner = authenticationService.getLoggedUser()?.id
			calendar = CalendarFactory(dateRepository).
					create(
					owner,
					currentWeek,
					Calendar.Source.LOCAL
			)
			saveCalendar.with(calendar).execute()
		}
		return calendar
	}
}