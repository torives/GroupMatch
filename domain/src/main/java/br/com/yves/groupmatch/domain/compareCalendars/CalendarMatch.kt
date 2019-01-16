package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

data class CalendarMatch(val result: Map<TimeSlot, Set<SessionMember>>) {
	private val _slots by lazy { result.keys.toList().sortedBy { it.start } }
	private val _participants by lazy { result.values.toSet() }

	val slots = _slots
	val participants = _participants
}