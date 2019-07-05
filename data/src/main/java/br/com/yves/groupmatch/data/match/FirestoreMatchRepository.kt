package br.com.yves.groupmatch.data.match

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.match.Match
import br.com.yves.groupmatch.domain.match.MatchRepository
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.models.slots.TimeSlot
import br.com.yves.groupmatch.domain.user.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreMatchRepository : MatchRepository {
    private val firestore get() = FirebaseFirestore.getInstance()

    override fun getMatch(matchId: String, callback: MatchRepository.GetMatchCallback) {
        firestore.collection(MATCH_COLLECTION).document(matchId).get()
                .addOnSuccessListener { document ->
                    val match = FirestoreMatchMapper.from(document)
                    callback.onSuccess(match)
                }.addOnFailureListener {
                    callback.onFailure(Error(it))
                }
    }

    override fun getMatches(matchIds: List<String>, callback: MatchRepository.GetMatchesCallback) {
        firestore.collection(MATCH_COLLECTION).get()
                .addOnSuccessListener { query ->
                    val matches = query.documents
                            .filter { matchIds.contains(it.id) }
                            .map { FirestoreMatchMapper.from(it) }
                    callback.onSuccess(matches)
                }.addOnFailureListener {
                    callback.onFailure(Error(it))
                }
    }

    override fun startMatch(group: Group, creator: User, localCalendar: Calendar, callback: MatchRepository.StartMatchCallback) {
        val match = FirestoreMatchMapper.from(group, creator, localCalendar)
        firestore.collection(MATCH_COLLECTION).document().set(match)
                .addOnSuccessListener { callback.onSuccess() }
                .addOnFailureListener { callback.onFailure(Error(it)) }
    }

    companion object {
        private const val MATCH_COLLECTION = "matches"
    }
}

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

    fun from(group: Group, creator: User, localCalendar: Calendar) = mapOf(
            "group" to mapOf(
                    "id" to group.id,
                    "name" to group.name
            ),
            "participants" to group.members.map {
                mapOf(
                        "id" to it.id,
                        "name" to it.name
                )
            },
            "creator" to mapOf(
                    "id" to creator.id,
                    "name" to creator.name,
                    "localCalendar" to mapOf(
                            "owner" to mapOf(
                                    "id" to creator.id,
                                    "name" to creator.name
                            ),
                            "week" to mapOf(
                                    "start" to localCalendar.week.start.toString(),
                                    "end" to localCalendar.week.end.toString()
                            ),
                            "events" to localCalendar.calendarTimeSlots
                                    .filter { it.isBusy }
                                    .map {
                                        mapOf(
                                                "start" to it.start.toString(),
                                                "end" to it.end.toString()
                                        )
                                    }
                    )
            )
    )
}

//data class FirestoreMatch(
//        var group: SimpleGroup,
//        var participants: List<SimpleUser>,
//        var creator: MatchCreator
//)
//
//data class SimpleGroup(
//        var id: String = "",
//        var name: String = ""
//)
//
//open class SimpleUser(
//        var id: String = "",
//        var name: String = ""
//)
//
//class MatchCreator(
//        id: String = "",
//        name: String = "",
//        var localCalendar: FirestoreCalendar
//) : SimpleUser(id, name)
//
//data class FirestoreCalendar(
//        var owner: SimpleUser,
//        var week: SimpleWeek,
//        var events: List<Event>
//)
//
//data class SimpleWeek(
//        var start: String = "",
//        var end: String = ""
//)
//
//data class Event(
//        var start: String = "",
//        var end: String = ""
//)