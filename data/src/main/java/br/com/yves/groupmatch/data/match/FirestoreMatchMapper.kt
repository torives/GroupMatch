package br.com.yves.groupmatch.data.match

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.match.Match
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.slots.TimeSlot
import br.com.yves.groupmatch.domain.user.User
import com.google.firebase.firestore.DocumentSnapshot

object FirestoreMatchMapper {
	fun from(document: DocumentSnapshot): Match {
		val data = document.data!!
		val status = Match.Status.valueOf(data["status"] as String)
		val groupId = (data["group"] as Map<*, *>)["id"] as String

		//FIXME: vai dar merda
		val result = data["result"] as? List<TimeSlot>

		return Match(
				document.id,
				status,
				groupId,
				result
		)
	}

	fun from(group: Group, creator: User, localCalendar: Calendar): RemoteMatch {
		val simpleGroup = SimpleGroup(group.id, group.name)
		val participants = group.members.map { SimpleUser(it.id, it.name) }

		val calendar = FirestoreCalendar(
				SimpleUser(creator.id, creator.name),
				SimpleWeek(localCalendar.week.start.toString(), localCalendar.week.end.toString()),
				localCalendar.calendarTimeSlots.map { Event(it.start.toString(), it.end.toString()) }
		)
		val matchCreator = MatchCreator(creator.id, creator.name, calendar)

		return RemoteMatch(simpleGroup, participants, matchCreator)
	}
}

data class RemoteMatch(
		var group: SimpleGroup,
		var participants: List<SimpleUser>,
		var creator: MatchCreator
)

data class SimpleGroup(
		var id: String = "",
		var name: String = ""
)

open class SimpleUser(
		var id: String = "",
		var name: String = ""
)

class MatchCreator(
		id: String = "",
		name: String = "",
		var localCalendar: FirestoreCalendar
) : SimpleUser(id, name)

data class FirestoreCalendar(
		var owner: SimpleUser,
		var week: SimpleWeek,
		var events: List<Event>
)

data class SimpleWeek(
		var start: String = "",
		var end: String = ""
)

data class Event(
		var start: String = "",
		var end: String = ""
)