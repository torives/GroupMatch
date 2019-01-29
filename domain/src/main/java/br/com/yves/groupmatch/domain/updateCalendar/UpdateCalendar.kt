package br.com.yves.groupmatch.domain.updateCalendar

import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

class UpdateCalendar(private val repository: TimeSlotRepository) : UseCase<Unit>() {

	private lateinit var timeSlots: List<TimeSlot>

	fun with(slots: List<TimeSlot>): UpdateCalendar {
		this.timeSlots = slots
		return this
	}

	override fun execute() {
		for (timeSlot in timeSlots) {
			repository.update(timeSlot)
		}
	}
}