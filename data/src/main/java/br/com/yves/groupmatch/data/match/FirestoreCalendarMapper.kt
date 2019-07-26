package br.com.yves.groupmatch.data.match

import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.user.User

object FirestoreCalendarMapper {
	fun from(creator: User, localCalendar: Calendar) = FirestoreCalendar(
			SimpleUser(creator.id, creator.name),
			SimpleWeek(localCalendar.week.start.toString(), localCalendar.week.end.toString()),
			localCalendar.calendarTimeSlots
					.filter { it.isBusy }
					.map { Event(it.start.toString(), it.end.toString()) }
	)
}

object FirestoreAnswerMapper {
	fun from(user: User, localCalendar: Calendar) = FirestoreAnswer(
			userId = user.id,
			localCalendar = FirestoreCalendarMapper.from(user, localCalendar)
	)
}


data class FirestoreAnswer(
		val userId: String,
		val hasJoined: Boolean = true,
		val localCalendar: FirestoreCalendar
)