package br.com.yves.groupmatch.domain.updateCalendar

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import br.com.yves.groupmatch.domain.showCalendar.TimeSlotRepository

class UpdateCalendar(private val repository: TimeSlotRepository) : UseCase<Unit>() {
	private lateinit var timeSlot: TimeSlot

	fun with(slot: TimeSlot): UpdateCalendar {
		this.timeSlot = slot
		return this
	}

	override fun execute() {
		repository.deleteTimeSlot(timeSlot)
	}
}