package br.com.yves.groupmatch.domain.updateCalendar

import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.TimeSlot

class UpdateCalendar(
	private val repository: TimeSlotRepository
) : UseCase<Unit>() {

	private lateinit var timeSlot: TimeSlot

	fun with(slot: TimeSlot): UpdateCalendar {
		this.timeSlot = TimeSlot(slot.date, !slot.isBusy)
		return this
	}

	override fun execute() {
		repository.update(timeSlot)
	}
}