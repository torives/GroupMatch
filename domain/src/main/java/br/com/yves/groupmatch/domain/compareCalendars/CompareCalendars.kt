package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.Week
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendar
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendar
import org.threeten.bp.LocalDateTime


data class ClientCalendar(
		val owner: String,
		val dates: List<LocalDateTime>, //BusyDates
		val weekStart: LocalDateTime,
		val weekEnd: LocalDateTime
)


class MergedTimeSlotBuilder {
	private lateinit var date: LocalDateTime
	private lateinit var status: Map<MatchSessionMember, Boolean>

	fun date(date: LocalDateTime) = apply { this.date = date }
	fun status(status: Map<MatchSessionMember, Boolean>) = apply { this.status = status }

	fun build(): MergedTimeSlot = MergedTimeSlot(date, status)
}

data class MergedTimeSlot(val date: LocalDateTime, val sessionMemberStatus: Map<MatchSessionMember, Boolean>) {
	val isCompletelyBusy: Boolean
		get() {
			for (isBusy in sessionMemberStatus.values) {
				if (isBusy.not()) {
					return false
				}
			}
			return true
		}
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

		//Cria mapa de dias ocupados para usuarios ocupados naquele dia
		//Cria lista MergedTimeSlot
		// Cria Span

		val clientCalendars = listOf<ClientCalendar>()
		val busyDates = mutableMapOf<LocalDateTime, MutableSet<MatchSessionMember>>()
		clientCalendars.forEach { calendar ->
			calendar.dates.forEach { date ->
				val ownerList = busyDates[date]
				if (ownerList == null) {
					setOf(calendar.owner)
				} else {
					ownerList.add(MatchSessionMember(calendar.owner))
				}
			}
		}

//		val week = getReferenceWeekFrom(clientCalendars)
//		val weekDates = dateRepository.getAllDatesFrom(week)
		val allUsers = setOf<MatchSessionMember>()
		val weekDates = listOf<LocalDateTime>()
		val mergedTimeSlots = mutableListOf<MergedTimeSlot>()
		val builder = MergedTimeSlotBuilder()
		weekDates.forEach { date ->
			builder.date(date)
			val userSet = mutableMapOf<MatchSessionMember, Boolean>()

			if (busyDates.containsKey(date)) {
				busyDates[date]?.forEach { user ->
					userSet[user] = true
				}
			} else {
				allUsers.forEach { user ->
					userSet[user] = false
				}
			}
			builder.status(userSet)
			mergedTimeSlots.add(builder.build())
		}


		//CREATE SPAN
		val matchSlots = mutableListOf<MatchFreeSlot>()
		val matchSlotBuilder = MatchSlotBuilder()
		var isBuildingSpan = false
		val memberStatus = mutableMapOf<MatchSessionMember, Boolean>()
		mergedTimeSlots.forEachIndexed { index, mergedSlot ->
			if (mergedSlot.isCompletelyBusy) {
				if (isBuildingSpan) {
					val endDate = mergedTimeSlots[index-1].date
					matchSlotBuilder.end(endDate)
					matchSlotBuilder.memberStatus(memberStatus)
					matchSlots.add(matchSlotBuilder.build())

					isBuildingSpan = false
					memberStatus.clear()
				}
			} else {
				if (isBuildingSpan) {

				} else {
					isBuildingSpan = true
					matchSlotBuilder.start(mergedSlot.date)
					memberStatus.putAll(mergedSlot.sessionMemberStatus)
				}
			}
		}


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