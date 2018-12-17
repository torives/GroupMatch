package br.com.yves.groupmatch.domain

import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendar
import br.com.yves.groupmatch.domain.showCalendar.Calendar
import br.com.yves.groupmatch.domain.showCalendar.TimeSlot

object CompareCalendarsFactory {
	fun create(
			calendars: List<BusyCalendar>,
			createCalendar: CreateCalendar
	) = CompareCalendars(calendars, createCalendar)
}

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
		return calendars.firstOrNull()?.referenceWeek
	}

	private fun areAllCalendarsInSameWeek(calendars: List<BusyCalendar>, week: Week): Boolean {

		if (calendars.isEmpty()) {
			throw IllegalArgumentException("Cannot validate empty calendar list")
		} else if (calendars.count() == 1) {
			return true
		}

		calendars.subList(1, calendars.lastIndex).forEach {
			if (it.referenceWeek != week)
				return false
		}
		return true
	}
}

data class User(val name: String)

data class MatchResult(
		val calendar: Calendar,
		val result: Map<TimeSlot, List<User>?>? = null //TODO: registrar quem está ocupado em cada slot
)