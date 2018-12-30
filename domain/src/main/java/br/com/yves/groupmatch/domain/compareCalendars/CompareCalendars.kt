package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.Week
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendar

class CompareCalendars(
		private val calendars: List<BusyCalendar>,
		private val createCalendar: CreateCalendar
) : UseCase<MatchResult>() {

	override fun execute(): MatchResult {
		val week = getReferenceWeekFrom(calendars)

		if (week == null) {
			throw IllegalArgumentException("Não há calendários para calcular o match")
		} else if (!areAllCalendarsInSameWeek(calendars, week)) {
			throw IllegalArgumentException("Os calendários recebidos não estão na mesma semana")
		}

		val busyDates = calendars.flatMap { it.busySlots }.map { it.date }
		val matchCalendar = createCalendar.with(week).execute()
		matchCalendar.forEach { timeSlot ->
			if (busyDates.contains(timeSlot.date)) {
				timeSlot.isBusy = true
			}
		}
		return MatchResult(matchCalendar)
	}

	private fun getReferenceWeekFrom(calendars: List<BusyCalendar>): Week? {
		val firstCalendar = calendars.firstOrNull()
		return firstCalendar?.weekStart?.rangeTo(firstCalendar.weekEnd)
	}

	private fun areAllCalendarsInSameWeek(calendars: List<BusyCalendar>, week: Week): Boolean {

		require(calendars.isEmpty().not()) {
			"Cannot validate empty calendar list"
		}

		if (calendars.count() == 1) {
			return true
		}

		calendars.subList(1, calendars.lastIndex).forEach {
			if (it.weekStart != week.start || it.weekEnd != week.endInclusive)
				return false
		}
		return true
	}
}