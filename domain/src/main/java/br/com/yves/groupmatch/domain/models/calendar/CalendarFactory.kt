package br.com.yves.groupmatch.domain.models.calendar

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot


class CalendarFactory(private val dateRepository: DateRepository) {
	fun create(owner: String, week: Week, source: Calendar.Source): Calendar {
		val timeSlots = timeSlotsFor(week)
		return Calendar(
				owner = owner,
				week = week,
				timeSlots = timeSlots,
				source = source
		)
	}

	private fun timeSlotsFor(week: Week): MutableList<TimeSlot> {
		return dateRepository.getAllDatesFrom(week).map { date ->
			TimeSlot(
					start = date,
					end = date.plusHours(1),
					isBusy = false
			)
		}.toMutableList()
	}
}