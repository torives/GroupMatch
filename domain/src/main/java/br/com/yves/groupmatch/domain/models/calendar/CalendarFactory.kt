package br.com.yves.groupmatch.domain.models.calendar

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.slots.CalendarTimeSlot


class CalendarFactory(private val dateRepository: DateRepository) {
	fun create(owner: String? = null, week: Week, source: Calendar.Source): Calendar {
		val timeSlots = timeSlotsFor(week)
		return Calendar(
				owner = owner,
				week = week,
				calendarTimeSlots = timeSlots,
				source = source
		)
	}

	private fun timeSlotsFor(week: Week): MutableList<CalendarTimeSlot> {
		return dateRepository.getAllDatesFrom(week).map { date ->
			CalendarTimeSlot(
					start = date,
					end = date.plusHours(1),
					isBusy = false
			)
		}.toMutableList()
	}
}