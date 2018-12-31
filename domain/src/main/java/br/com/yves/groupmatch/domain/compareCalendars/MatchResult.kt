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
		val start: LocalDateTime,
		val end: LocalDateTime,
		val sessionMembers: Set<MatchSessionMember>
)


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
