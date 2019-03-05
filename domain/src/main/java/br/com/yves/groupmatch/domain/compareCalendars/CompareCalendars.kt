package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.models.slots.TimeSlot
import br.com.yves.groupmatch.domain.models.slots.TimeSlotBuilder
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.slots.CalendarTimeSlot
import org.threeten.bp.LocalDateTime

class CompareCalendars(
		private val calendars: List<Calendar>,
		private val dateRepository: DateRepository
) : UseCase<CalendarMatch>() {
	init {
		require(calendars.isNotEmpty()) {
			"Failed to instantiate ${this::class.java.name}. Can't compare calendars with empty calendar list"
		}
		require(areCalendarsComparable()) {
			"Failed to instantiate ${this::class.java.name}. Check if calendars reference the same week and have the same number of ${CalendarTimeSlot::class.java.simpleName}s"
		}
	}

	override fun execute(): CalendarMatch {

		fun consolidateBusyStatus(slots: List<TimeSlot>, calendarTimeSlots: List<List<CalendarTimeSlot>>) {
			slots.forEachIndexed { index, slot ->
				calendarTimeSlots.forEach { timeSlots ->
					slot.isBusy = slot.isBusy || timeSlots[index].isBusy
				}
			}
		}

		fun generateMatchResult(consolidatedSlots: List<TimeSlot>): List<TimeSlot> {
			val result = mutableListOf<TimeSlot>()
			val builder = TimeSlotBuilder()

			fun closeAndBuild(end: LocalDateTime) {
				builder.setEnd(end)
				result.add(builder.build())
				builder.clear()
			}

			consolidatedSlots.forEachIndexed { index, slot ->
				if (!builder.isEmpty) {
					when {
						slot.isBusy -> {
							closeAndBuild(slot.start)
						}
						slot.start.dayOfWeek != builder.start?.dayOfWeek -> {
							closeAndBuild(slot.start)
							builder.setStart(slot.start)
						}
						consolidatedSlots.indices.last == index -> {
							closeAndBuild(slot.end)
						}
					}
				} else if (!slot.isBusy) {
					builder.setStart(slot.start)
				}
			}
			return result
		}

		val matchSlots = calendars.first().calendarTimeSlots.map { MatchTimeSlotMapper.map(it) }
		val calendarTimeSlots = calendars.map { it.calendarTimeSlots }

		consolidateBusyStatus(matchSlots, calendarTimeSlots)
		val matchResult = generateMatchResult(matchSlots)


		return CalendarMatch()
	}

	private fun areCalendarsComparable(): Boolean {
		return areCalendarsWellFormed() && areCalendarsReferencingSameWeek()
	}

	private fun areCalendarsWellFormed(): Boolean {
		for (calendar in calendars) {
			if (calendar.calendarTimeSlots.size != TIMESLOTS_PER_WEEK)
				return false
		}
		return true
	}

	private fun areCalendarsReferencingSameWeek(): Boolean {
		val weeks = calendars.map { it.week }
		val current = weeks.first()

		for (i in 1 until weeks.lastIndex) {
			if (current != weeks[i]) {
				return false
			}
		}
		return true
	}

	companion object {
		private const val TIMESLOTS_PER_WEEK = 168 //24h per day, 7 days per week
	}
}
class CalendarMatch