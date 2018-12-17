package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.showCalendar.Calendar
import br.com.yves.groupmatch.domain.showCalendar.TimeSlot

data class MatchResult(
		val calendar: Calendar,
		val result: Map<TimeSlot, List<User>?>? = null //TODO: registrar quem está ocupado em cada slot
)