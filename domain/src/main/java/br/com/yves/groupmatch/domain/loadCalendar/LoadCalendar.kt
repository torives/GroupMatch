package br.com.yves.groupmatch.domain.loadCalendar

import br.com.yves.groupmatch.domain.*
import br.com.yves.groupmatch.domain.saveCalendar.SaveCalendar
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar

class LoadCalendar(
	private val dateRepository: DateRepository,
	private val timeSlotRepository: TimeSlotRepository,
	private val createCalendar: CreateCalendar,
	private val saveCalendar: SaveCalendar
) : UseCase<Calendar>() {

	override fun execute(): Calendar {
		val currentWeek = dateRepository.getCurrentWeek()
		val timeSlots = timeSlotRepository.timeSlotsBetween(
			currentWeek.start,
			currentWeek.endInclusive
		)
		return when (timeSlots.isEmpty()) {
			true -> {
				val calendar = createCalendar.with(currentWeek, "").execute()
				saveCalendar.with(calendar).execute()

				ca
			}
			false -> timeSlots
		}
	}
}

