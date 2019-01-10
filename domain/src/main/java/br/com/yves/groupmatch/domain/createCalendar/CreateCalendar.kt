package br.com.yves.groupmatch.domain.createCalendar

import br.com.yves.groupmatch.domain.*
import br.com.yves.groupmatch.domain.sendCalendar.CalendarFactory

class CreateCalendar(private val dateRepository: DateRepository) : UseCase<Calendar>() {

	private lateinit var week: Week
	private lateinit var owner: String

	fun with(week: Week, owner: String) = apply {
		this.week = week
		this.owner = owner
	}

	override fun execute(): Calendar {
		val timeSlots = dateRepository.getAllDatesFrom(this.week).map {
			TimeSlot(it, it.plusHours(1), false)
		}

		return CalendarFactory.create(owner, week, timeSlots, Calendar.Source.LOCAL)
	}
}