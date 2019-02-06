package br.com.yves.groupmatch.domain.updateCalendar

import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.TimeSlotRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

class UpdateCalendar(private val repository: CalendarRepository) : UseCase<Unit>() {

	private lateinit var timeSlot: TimeSlot

	fun with(timeSlot: TimeSlot): UpdateCalendar {
		this.timeSlot = timeSlot
		return this
	}

	override fun execute() {
		repository.getCalendar(timeSlot.calendarId)?.apply {
			val index = timeSlots.indexOf(timeSlot)
			if (index >= 0){
				timeSlots[index] = timeSlot
			}
			repository.update(this)
		}
	}
}