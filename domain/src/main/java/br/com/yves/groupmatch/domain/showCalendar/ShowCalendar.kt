package br.com.yves.groupmatch.domain.showCalendar

import br.com.yves.groupmatch.domain.UseCase

typealias Calendar = List<TimeSlot>

class ShowCalendar(
	private val dateRepository: DateRepository,
	private val timeSlotRepository: TimeSlotRepository
) : UseCase<Calendar>() {

	override fun execute(): Calendar {
		val currentWeek = dateRepository.getCurrentWeek()
		val weekDates = dateRepository.getAllDatesFrom(currentWeek)
		val busyTimeSlots = timeSlotRepository.getTimeSlotsBetween(
			currentWeek.start,
			currentWeek.endInclusive
		)

		return weekDates.map { dateTime ->
			val isBusy = busyTimeSlots.find { it.date == dateTime && it.isBusy } != null
			TimeSlot(dateTime, isBusy)
		}
	}
}

