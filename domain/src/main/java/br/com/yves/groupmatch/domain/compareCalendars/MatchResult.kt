package br.com.yves.groupmatch.domain.compareCalendars

import org.threeten.bp.LocalDateTime

data class MatchResult(
		val slots: List<MatchFreeSlot>
)

data class MatchFreeSlot(
		var start: LocalDateTime,
		var end: LocalDateTime,
		val sessionMembers: Set<MatchSessionMember> = mutableSetOf()
) {

	fun canMerge(other: MatchFreeSlot): Boolean {
		return other.end == this.start || other.start == this.end
	}

	fun merge(other: MatchFreeSlot): MatchFreeSlot {
		require(other.end == this.start || other.start == this.end) {
			"Cannot merge slots that aren't contiguous"
		}
		val ordered = listOf(this, other).sortedBy { it.start }
		val newMembers = this.sessionMembers.intersect(other.sessionMembers)

		return MatchFreeSlot(ordered.first().start, ordered.last().end, newMembers)
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
