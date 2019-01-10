package br.com.yves.groupmatch.domain.updateCalendar

import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.TimeSlot
import br.com.yves.groupmatch.domain.UseCase

class UpdateCalendar(
		private val repository: CalendarRepository
) : UseCase<Unit>() {

	private lateinit var timeSlots: List<TimeSlot>

	fun with(slots: List<TimeSlot>): UpdateCalendar {
		this.timeSlots = slots
		return this
	}

	override fun execute() {
		repository.getCalendar()?.apply {
			repository.update(week, timeSlots)
		} ?: run {
			throw IllegalStateException("Attempt to update calendar before creating one")
		}
	}
}