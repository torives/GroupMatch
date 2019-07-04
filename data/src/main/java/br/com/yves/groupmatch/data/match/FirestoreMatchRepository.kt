package br.com.yves.groupmatch.data.match

import br.com.yves.groupmatch.domain.match.Match
import br.com.yves.groupmatch.domain.match.MatchRepository
import br.com.yves.groupmatch.domain.models.slots.TimeSlot
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
                    callback.onFailure(Error())
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
                    callback.onFailure(Error())
                }
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
}