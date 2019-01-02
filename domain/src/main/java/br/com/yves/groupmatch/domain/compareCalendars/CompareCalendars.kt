package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.DateRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.Week
import br.com.yves.groupmatch.domain.sendCalendar.ClientCalendar
import org.threeten.bp.LocalDateTime


class MergedTimeSlotBuilder {
	private lateinit var date: LocalDateTime
	private lateinit var status: Map<MatchSessionMember, Boolean>

	fun date(date: LocalDateTime) = apply { this.date = date }
	fun status(status: Map<MatchSessionMember, Boolean>) = apply { this.status = status }

	fun build(): MergedTimeSlot = MergedTimeSlot(date, status)
}

data class MergedTimeSlot(val date: LocalDateTime, val sessionMemberStatus: Map<MatchSessionMember, Boolean>)


class CompareCalendars(
		private val clientCalendars: List<ClientCalendar>,
		private val dateRepository: DateRepository
) : UseCase<MatchResult>() {

	private val allSessionUsers: Set<MatchSessionMember> by lazy {
		clientCalendars.map { MatchSessionMember(it.owner) }.toSet()
	}

	override fun execute(): MatchResult {
		check(clientCalendars.isNotEmpty()) {
			"Cannot compare calendars with empty calendar list"
		}

		val weekDates = getAllDatesFromReferenceWeek(clientCalendars)
		val busyDates = mapBusyDatesToMatchSessionMember(clientCalendars)
		val mergedTimeSlots = createMergedTimeSlots(weekDates, busyDates)

		//FreeSlots
		val freeMergedSlots = mergedTimeSlots.map { timeSlot ->
			val freeMembers = timeSlot.sessionMemberStatus.mapNotNull { (member, isBusy) ->
				if (isBusy.not()) member else null
			}
			Pair(timeSlot.date, freeMembers.toSet())
		}

		val trueSlots = freeMergedSlots.map {
			/*
			 cada MatchFreeSlot representa um range de horário. TimeSlot representa um limite de horãrio apenas.
			 ent"ao pra contruir um MatchFreeSlot vc precisa de dois TimeSlots.

			 se o primeiro timeslot é livre e o segundo eh ocupado
			 é um MatchFreeSlot

			 se o primeiro timeslot é ocupado e o segundo é livre,
			 não ~~ um MatchFreeSlot
			* */
			MatchFreeSlot(freeMergedSlots.first().first, freeMergedSlots.first().first.plusHours(1), freeMergedSlots.first().second)
		}
		trueSlots.sortedBy { it.start }
		val result = mutableListOf<MatchFreeSlot>()
		var current = trueSlots.first()
		result.add(current)
		for (i in 1 until trueSlots.lastIndex) {
			val next = trueSlots[i]

			current = if (current.canMerge(next)) {
				val new = current.merge(next)
				result.add(new)

				new
			} else {
				next
			}
		}

		result.sortedBy { it.sessionMembers.size }

		return MatchResult(result)
	}

	private fun createMergedTimeSlots(
			weekDates: List<LocalDateTime>,
			busyDates: Map<LocalDateTime, Set<MatchSessionMember>>
	): List<MergedTimeSlot> {

		val mergedTimeSlots = mutableListOf<MergedTimeSlot>()
		val builder = MergedTimeSlotBuilder()

		weekDates.forEach { date ->
			builder.date(date)
			val userSet = mutableMapOf<MatchSessionMember, Boolean>()

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

	private fun getAllDatesFromReferenceWeek(calendars: List<ClientCalendar>): List<LocalDateTime> {
		val referenceWeek = getReferenceWeek(calendars)
		return dateRepository.getAllDatesFrom(referenceWeek)
	}

	private fun mapBusyDatesToMatchSessionMember(calendars: List<ClientCalendar>): Map<LocalDateTime, Set<MatchSessionMember>> {
		val busyDates = mutableMapOf<LocalDateTime, MutableSet<MatchSessionMember>>()

		calendars.forEach { calendar ->
			calendar.busyDates.forEach { date ->
				busyDates[date]?.apply {
					add(MatchSessionMember(calendar.owner))
				} ?: run {
					busyDates[date] = mutableSetOf(MatchSessionMember(calendar.owner))
				}
			}
		}
		return busyDates
	}

	private fun getReferenceWeek(calendars: List<ClientCalendar>): Week {
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