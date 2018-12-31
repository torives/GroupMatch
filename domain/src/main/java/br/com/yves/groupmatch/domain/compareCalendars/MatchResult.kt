package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.showCalendar.Calendar
import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import org.threeten.bp.LocalDateTime

data class MatchResult(
		val calendar: Calendar,
		val result: Map<TimeSlot, List<User>?>? = null //TODO: registrar quem está ocupado em cada slot,
)


typealias Match = List<MatchFreeSlot>

data class MatchFreeSlot(
		var start: LocalDateTime? = null,
		var end: LocalDateTime? = null,
		val sessionMembers: MutableSet<MatchSessionMember> = mutableSetOf()
) {
	companion object {
		fun merge(first: MatchFreeSlot, second: MatchFreeSlot): MatchFreeSlot {
			require(first.start!! >= second.end &&first.end!! <= second.start) {
				"Cannot merge slots that aren't contiguous"
			}

			val newSlot = MatchFreeSlot()
			val newerStart = if (first.start!! >= second.start) first.start else second.start
			val furthestEnd = if (first.end!! >= second.end) first.end else second.end
			newSlot.start = newerStart
			newSlot.end = furthestEnd
			newSlot.sessionMembers.intersect(second.sessionMembers)

			return newSlot
		}
	}
}


class MatchSlotBuilder {
	private lateinit var start: LocalDateTime
	private lateinit var end: LocalDateTime
	private lateinit var members: Set<MatchSessionMember>

	fun start(date: LocalDateTime) = apply { this.start = date }
	//TODO: Conferir se end >= start
	fun end(date: LocalDateTime) = apply { this.end = date }

	fun members(members: Set<MatchSessionMember>) = apply { this.members = members }

	fun build(): MatchFreeSlot = MatchFreeSlot(start, end, members)
}

data class MatchSessionMember(val name: String)
