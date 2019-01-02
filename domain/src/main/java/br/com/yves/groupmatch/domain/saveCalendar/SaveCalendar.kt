package br.com.yves.groupmatch.domain.saveCalendar

import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.loadCalendar.Calendar


class SaveCalendar(private val timeSlotRepository: TimeSlotRepository) : UseCase<Unit>() {
	private lateinit var calendar: Calendar

	fun with(calendar: Calendar): SaveCalendar {
		this.calendar = calendar
		return this
	}

	override fun execute() {
		timeSlotRepository.insert(calendar)
	}
}