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

	private class MergedTimeSlotBuilder {
		private lateinit var date: LocalDateTime
		private lateinit var status: Map<SessionMember, Boolean>

		fun date(date: LocalDateTime) = apply { this.date = date }
		fun status(status: Map<SessionMember, Boolean>) = apply { this.status = status }

		fun build(): MergedTimeSlot = MergedTimeSlot(date, status)
	}

	private data class MergedTimeSlot(val date: LocalDateTime, val sessionMemberStatus: Map<SessionMember, Boolean>)


	private val allSessionUsers: Set<SessionMember> by lazy {
		calendars.map { SessionMember(it.owner) }.toSet()
	}

	override fun execute(): CalendarMatch {

		val week = calendars.first().week
		val resultSlots = calendars.first().timeSlots.map { MatchTimeSlot(it.start, it.end, it.isBusy) }
		val timeSlots = calendars.map { it.timeSlots }

		resultSlots.forEachIndexed { index, slot ->
			timeSlots.forEach { timeSlots ->
				slot.isBusy = slot.isBusy || timeSlots[index].isBusy
			}
		}

		resultSlots.forEach { slot ->
			
		}


//		val busyDates = mapBusyDatesToMatchSessionMember(calendars)
//		val mergedTimeSlots = createMergedTimeSlots(weekDates, busyDates)
//
//		//FreeSlots
//		val freeMergedSlots = mergedTimeSlots.map { timeSlot ->
//			val freeMembers = timeSlot.sessionMemberStatus.mapNotNull { (member, isBusy) ->
//				if (isBusy.not()) member else null
//			}
//			Pair(timeSlot.date, freeMembers.toSet())
//		}
//
//		val trueSlots = freeMergedSlots.map {
//			/*
//			 cada MatchTimeSlot representa um range de horário. TimeSlot representa um limite de horãrio apenas.
//			 ent"ao pra contruir um MatchTimeSlot vc precisa de dois TimeSlots.
//
//			 se o primeiro timeslot é livre e o segundo eh ocupado
//			 é um MatchTimeSlot
//
//			 se o primeiro timeslot é ocupado e o segundo é livre,
//			 não ~~ um MatchTimeSlot
//			* */
//			MatchTimeSlot(freeMergedSlots.first().first, freeMergedSlots.first().first.plusHours(1), freeMergedSlots.first().second)
//		}
//		trueSlots.sortedBy { it.start }
//		val result = mutableListOf<MatchTimeSlot>()
//		var current = trueSlots.first()
//		result.add(current)
//		for (i in 1 until trueSlots.lastIndex) {
//			val next = trueSlots[i]
//
//			current = if (current.canMerge(next)) {
//				val new = current.merge(next)
//				result.add(new)
//				new
//			} else {
//				next
//			}
//		}
//		val sortedResult = result.sortedBy { it.sessionMembers.size }

		return CalendarMatch()
	}

	private fun createMergedTimeSlots(
			weekDates: List<LocalDateTime>,
			busyDates: Map<LocalDateTime, Set<SessionMember>>
	): List<MergedTimeSlot> {

		val mergedTimeSlots = mutableListOf<MergedTimeSlot>()
		val builder = MergedTimeSlotBuilder()

		weekDates.forEach { date ->
			builder.date(date)
			val userSet = mutableMapOf<SessionMember, Boolean>()

			if (busyDates.containsKey(date)) {
				busyDates.getValue(date).forEach { user ->
					userSet[user] = true
				}
			} else {
				allSessionUsers.forEach { user ->
					userSet[user] = false
				}
			}
			builder.status(userSet)
			mergedTimeSlots.add(builder.build())
		}
		return mergedTimeSlots
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

	private fun mapBusyDatesToMatchSessionMember(calendars: List<Calendar>): Map<LocalDateTime, Set<SessionMember>> {
		val busyDates = mutableMapOf<LocalDateTime, MutableSet<SessionMember>>()

		calendars.forEach { calendar ->
			calendar.timeSlots.forEach { slot ->
				if (slot.isBusy) {
					busyDates[slot.start]?.apply {
						add(SessionMember(calendar.owner))
					} ?: run {
						busyDates[slot.start] = mutableSetOf(SessionMember(calendar.owner))
					}
				}
			}
		}
		return busyDates
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


class CalendarMatch