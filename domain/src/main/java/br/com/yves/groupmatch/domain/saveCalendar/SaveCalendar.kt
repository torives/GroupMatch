package br.com.yves.groupmatch.domain.saveCalendar

import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.UseCase


class SaveCalendar(private val calendarRepository: CalendarRepository) : UseCase<Unit>() {
	private lateinit var calendar: Calendar

	fun with(calendar: Calendar): SaveCalendar {
		this.calendar = calendar
		return this
	}

	override fun execute() {
		calendarRepository.insert(calendar)
	}
}