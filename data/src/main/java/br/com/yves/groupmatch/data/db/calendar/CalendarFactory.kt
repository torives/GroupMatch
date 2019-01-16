package br.com.yves.groupmatch.data.db.calendar

import br.com.yves.groupmatch.domain.*
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot


class CalendarFactory(private val dateRepository: DateRepository) {
	fun create(owner: String, week: Week, source: Calendar.Source) {

	}

	private fun timeSlotsFor(week: Week): List<TimeSlot> {
		val timeSlots = mutableListOf<TimeSlot>()
		val dates = dateRepository.getAllDatesFrom(week)
		val current = dates.indices.first
		val next = current+1
		while (next <= dates.indices.last) {
			timeSlots.add(TimeSlotRoom())
		}
	}
}