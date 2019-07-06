package br.com.yves.groupmatch.data.match

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.match.MatchRepository
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.user.User
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FirestoreMatchRepository(private val service: GroupMatchService) : MatchRepository {
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
        service.startMatch(match).enqueue(object : Callback<GroupMatchService.Response> {

            override fun onFailure(call: Call<GroupMatchService.Response>, t: Throwable) {
                callback.onFailure(Error(t))
            }

            override fun onResponse(call: Call<GroupMatchService.Response>, response: Response<GroupMatchService.Response>) {
                if(response.isSuccessful) {
                    callback.onSuccess()
                } else {
                    callback.onFailure(Error(response.body()?.message))
                }
            }
        })
    }

    companion object {
        private const val MATCH_COLLECTION = "matches"
    }
}

