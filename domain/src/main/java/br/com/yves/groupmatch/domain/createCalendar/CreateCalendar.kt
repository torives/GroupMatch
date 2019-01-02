package br.com.yves.groupmatch.domain.createCalendar

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.Week
import br.com.yves.groupmatch.domain.loadCalendar.Calendar
import br.com.yves.groupmatch.domain.loadCalendar.TimeSlot

class CreateCalendar(private val dateRepository: DateRepository) : UseCase<Calendar>() {

	private lateinit var week: Week

	fun with(week: Week): CreateCalendar {
		this.week = week
		return this
	}

	override fun execute(): Calendar {
		val dates = dateRepository.getAllDatesFrom(this.week)
		return dates.map {
			TimeSlot(it, false)
		}
	}
}