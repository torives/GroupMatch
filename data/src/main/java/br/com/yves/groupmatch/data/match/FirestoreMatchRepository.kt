package br.com.yves.groupmatch.data.match

import br.com.yves.groupmatch.data.group.FirestoreGroup
import br.com.yves.groupmatch.domain.match.Match
import br.com.yves.groupmatch.domain.match.MatchRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreMatchRepository : MatchRepository {
    private val firestore get() = FirebaseFirestore.getInstance()

    override fun getMatch(groupId: String, callback: MatchRepository.GetMatchCallback) {
        firestore.collection(MATCH_COLLECTION).document(groupId).get()
                .addOnSuccessListener { document ->
                    val match = FirestoreMatchMapper.from(document)
                    callback.onSuccess(match)
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
        val groupId = (data["group"] as Map<*, *>)["id"]

        val result = data["result"] as Array

        return Match(
                document.id,
                status,
                groupId,
                result
        )
    }
}