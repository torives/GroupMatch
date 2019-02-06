package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.models.Week
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import org.threeten.bp.LocalDateTime

class CompareCalendars(
		private val calendars: List<Calendar>,
		private val dateRepository: DateRepository
) : UseCase<CalendarMatch>() {

	private class MergedTimeSlotBuilder {
		private lateinit var date: LocalDateTime
		private lateinit var status: Map<SessionMember, Boolean>

		fun date(date: LocalDateTime) = apply { this.date = date }
		fun status(status: Map<SessionMember, Boolean>) = apply { this.status = status }

		fun build(): MergedTimeSlot = MergedTimeSlot(date, status)
	}

	private data class MergedTimeSlot(val date: LocalDateTime, val sessionMemberStatus: Map<SessionMember, Boolean>)

//	data class MatchFreeSlot(
//			override val start: LocalDateTime,
//			override val end: LocalDateTime,
//			val sessionMembers: Set<SessionMember> = mutableSetOf()
//	) : TimeSlot() {
//		override val isBusy: Boolean = false
//
//		init {
//			require(start < end) {
//				"Failed to instantiate ${this::class.java.name}. Start date ($start) must be smaller than end date ($end)"
//			}
//		}
//
//		fun canMerge(other: MatchFreeSlot): Boolean {
//			return other.end == this.start || other.start == this.end
//		}
//
//		fun merge(other: MatchFreeSlot): MatchFreeSlot {
//			require(other.end == this.start || other.start == this.end) {
//				"Cannot merge slots that aren't contiguous"
//			}
//			val ordered = listOf(this, other).sortedBy { it.start }
//			val newMembers = this.sessionMembers.intersect(other.sessionMembers)
//
//			return MatchFreeSlot(ordered.first().start, ordered.last().end, newMembers)
//		}
//	}

	private val allSessionUsers: Set<SessionMember> by lazy {
		calendars.map { SessionMember(it.owner) }.toSet()
	}

	override fun execute(): CalendarMatch {
//		check(calendars.isNotEmpty()) {
//			"Cannot compare calendars with empty calendar list"
//		}
//
//		val weekDates = getAllDatesFromReferenceWeek(calendars)
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
//			 cada MatchFreeSlot representa um range de horário. TimeSlot representa um limite de horãrio apenas.
//			 ent"ao pra contruir um MatchFreeSlot vc precisa de dois TimeSlots.
//
//			 se o primeiro timeslot é livre e o segundo eh ocupado
//			 é um MatchFreeSlot
//
//			 se o primeiro timeslot é ocupado e o segundo é livre,
//			 não ~~ um MatchFreeSlot
//			* */
//			MatchFreeSlot(freeMergedSlots.first().first, freeMergedSlots.first().first.plusHours(1), freeMergedSlots.first().second)
//		}
//		trueSlots.sortedBy { it.start }
//		val result = mutableListOf<MatchFreeSlot>()
//		var current = trueSlots.first()
//		result.add(current)
//		for (i in 1 until trueSlots.lastIndex) {
//			val next = trueSlots[i]
//
//			current = if (current.canMerge(next)) {
//				val new = current.merge(next)
//				result.add(new)
//
//				new
//			} else {
//				next
//			}
//		}
//
//		result.sortedBy { it.sessionMembers.size }

		return CalendarMatch(mapOf())
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
				busyDates[date]!!.forEach { user ->
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

	private fun getAllDatesFromReferenceWeek(calendars: List<Calendar>): List<LocalDateTime> {
		val referenceWeek = getReferenceWeek(calendars)
		return dateRepository.getAllDatesFrom(referenceWeek)
	}

	private fun mapBusyDatesToMatchSessionMember(calendars: List<Calendar>): Map<LocalDateTime, Set<SessionMember>> {
		val busyDates = mutableMapOf<LocalDateTime, MutableSet<SessionMember>>()

		calendars.forEach { calendar ->
			calendar.timeSlots.forEach { slot ->
				if (slot.isBusy){
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

	private fun getReferenceWeek(calendars: List<Calendar>): Week {
		val weeks = calendars.map { it.week }
		val current = weeks.first()

		for (i in 1 until weeks.lastIndex) {
			if (current != weeks[i]) {
				throw IllegalArgumentException("Cannot compare calendars with different reference weeks")
			}
		}
		return current
	}
}