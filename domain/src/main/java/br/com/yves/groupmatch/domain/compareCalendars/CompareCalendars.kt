package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.timeslot.Slot
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
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
			"Failed to instantiate ${this::class.java.name}. Check if calendars reference the same week and have the same number of ${TimeSlot::class.java.simpleName}s"
		}
	}

	override fun execute(): CalendarMatch {

		fun consolidateBusyStatus(matchSlots: List<MatchTimeSlot>, timeSlots: List<List<TimeSlot>>) {
			matchSlots.forEachIndexed { index, slot ->
				timeSlots.forEach { timeSlots ->
					slot.isBusy = slot.isBusy || timeSlots[index].isBusy
				}
			}
		}

		fun generateMatchResult(consolidatedSlots: List<MatchTimeSlot>): List<MatchTimeSlot> {
			val result = mutableListOf<MatchTimeSlot>()
			val builder = MatchTimeSlotBuilder()

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

		val matchSlots = calendars.first().timeSlots.map { MatchTimeSlotMapper.map(it) }
		val calendarTimeSlots = calendars.map { it.timeSlots }

		consolidateBusyStatus(matchSlots, calendarTimeSlots)
		val matchResult = generateMatchResult(matchSlots)


		return CalendarMatch()
	}

	private fun areCalendarsComparable(): Boolean {
		return areCalendarsWellFormed() && areCalendarsReferencingSameWeek()
	}

	private fun areCalendarsWellFormed(): Boolean {
		for (calendar in calendars) {
			if (calendar.timeSlots.size != TIMESLOTS_PER_WEEK)
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

data class MatchTimeSlot(
		override val start: LocalDateTime,
		override val end: LocalDateTime,
		override var isBusy: Boolean
) : Slot {
	init {
		require(start < end) {
			"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
		}
	}

	/**
	 * Merges **this** with another contiguous [MatchTimeSlot], possibly altering its [isBusy] status.
	 *
	 * @param other The [MatchTimeSlot] that will be merged with **this**
	 *
	 * @return A new [MatchTimeSlot], starting from the earliest date and ending on the furthest
	 * date between **this** and [other].
	 * The new slot is busy if **this** or [other] is busy
	 *
	 * @throws IllegalArgumentException If the slots aren't contiguous or have a different busy status
	 */
	fun merge(other: Slot): MatchTimeSlot {
		require(canMerge(other)) {
			"Cannot merge slots that aren't contiguous"
		}
		val ordered = listOf(this, other).sortedBy { it.start }

		return MatchTimeSlot(
				ordered.first().start,
				ordered.last().end,
				isBusy = ordered.first().isBusy
		)
	}

	private fun canMerge(other: Slot): Boolean {
		return (other.end == this.start || other.start == this.end) &&
				this.isBusy == other.isBusy
	}
}

private class MatchTimeSlotBuilder {
	var start: LocalDateTime? = null
		private set
	var end: LocalDateTime? = null
		private set
	var isEmpty = true

	fun setStart(start: LocalDateTime) = apply {
		this.start = start
		isEmpty = false
	}

	fun setEnd(end: LocalDateTime) = apply {
		this.end = end
		isEmpty = false
	}

	fun build() = MatchTimeSlot(start!!, end!!, false)

	fun clear() {
		start = null
		end = null
		isEmpty = true
	}
}

object MatchTimeSlotMapper {
	fun map(slot: Slot): MatchTimeSlot = MatchTimeSlot(
			slot.start,
			slot.end,
			slot.isBusy
	)
}


class CalendarMatch