package br.com.yves.groupmatch.domain.models.calendar

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlotImpl


class CalendarFactory(private val dateRepository: DateRepository) {
	fun create(owner: String, week: Week, source: Calendar.Source): Calendar {
		val timeSlots = timeSlotsFor(week)
		return CalendarImpl(owner, week, timeSlots, source)
	}

	private fun timeSlotsFor(week: Week): List<TimeSlot> {
		val timeSlots = mutableListOf<TimeSlot>()
		val dates = dateRepository.getAllDatesFrom(week)

		var current = dates.indices.first
		var next = current + 1
		do {
			timeSlots.add(
					TimeSlotImpl(
							dates[current],
							dates[next],
							false
					)
			)
			current = next
			next++
		} while (next <= dates.indices.last)

		return timeSlots
	}
}