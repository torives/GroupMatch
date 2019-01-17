package br.com.yves.groupmatch.domain.updateCalendar

import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import br.com.yves.groupmatch.domain.UseCase

class UpdateCalendar(
		private val repository: CalendarRepository,
		private val dateRepository: DateRepository
) : UseCase<Unit>() {

	private lateinit var timeSlots: List<TimeSlot>

	fun with(slots: List<TimeSlot>): UpdateCalendar {
		this.timeSlots = slots
		return this
	}

	override fun execute() {
		val currentWeek = dateRepository.getCurrentWeek()
		repository.getCalendar(currentWeek)?.let {
			repository.update(it)
		} ?: run {
			throw IllegalStateException("Attempt to update calendar before creating one")
		}
	}
}