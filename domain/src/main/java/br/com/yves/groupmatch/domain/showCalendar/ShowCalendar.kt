package br.com.yves.groupmatch.domain.showCalendar

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar

typealias Calendar = List<TimeSlot>

class ShowCalendar(
	private val dateRepository: DateRepository,
	private val timeSlotRepository: TimeSlotRepository,
	private val createCalendar: CreateCalendar
) : UseCase<Calendar>() {

	override fun execute(): Calendar {
		val currentWeek = dateRepository.getCurrentWeek()
		val timeSlots = timeSlotRepository.timeSlotsBetween(
			currentWeek.start,
			currentWeek.endInclusive
		)
		return when (timeSlots.isEmpty()) {
			true -> {
				createCalendar.with(currentWeek).execute()
				timeSlotRepository.timeSlotsBetween(
					currentWeek.start,
					currentWeek.endInclusive
				)
			}
			false -> timeSlots
		}
	}
}

