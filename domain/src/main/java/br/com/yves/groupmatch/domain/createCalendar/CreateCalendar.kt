package br.com.yves.groupmatch.domain.createCalendar

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.Week

class CreateCalendar(
	private val dateRepository: DateRepository,
	private val timeSlotRepository: TimeSlotRepository
) : UseCase<Unit>() {
	private lateinit var week: Week

	fun with(week: Week): CreateCalendar {
		this.week = week
		return this
	}

	override fun execute() {
		val dates = dateRepository.getAllDatesFrom(this.week)
		timeSlotRepository.insert(dates.map {
			TimeSlot(
				it,
				false
			)
		})
	}
}