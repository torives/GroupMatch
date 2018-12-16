package br.com.yves.groupmatch.domain

import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendar
import br.com.yves.groupmatch.domain.showCalendar.Calendar
import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import org.threeten.bp.LocalDateTime

class CompareCalendars(private val calendars: List<BusyCalendar>) : UseCase<MatchResult>() {

	override fun execute(): MatchResult {
		if (calendars.isEmpty()) {
			throw IllegalArgumentException("Não há calendários para calcular o match")
		} else if (!isTimePeriodTheSame(calendars)) {
			throw IllegalArgumentException("Os calendários recebidos não estão na mesma semana")
		}

		val result = mutableMapOf<LocalDateTime, Boolean>()
		calendars.flatMap { it.busySlots }
				.forEach { timeSlot ->
					result[timeSlot.date] = true
				}

		val matchCalendar = resultToCalendar(result)
		return MatchResult(matchCalendar)
	}

	private fun resultToCalendar(map: Map<LocalDateTime, Boolean>): Calendar {
		return map.entries.map {
			TimeSlot(it.key, it.value)
		}
	}

	private fun isTimePeriodTheSame(calendars: List<BusyCalendar>): Boolean {

		if (calendars.isEmpty()) {
			throw IllegalArgumentException("Cannot validate empty calendar list")
		}
		val week = calendars.first().referenceWeek

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