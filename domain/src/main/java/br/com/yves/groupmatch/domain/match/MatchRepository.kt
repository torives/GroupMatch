package br.com.yves.groupmatch.domain.match

interface MatchRepository {
    fun getMatch(groupId: String, callback: GetMatchCallback)

    interface GetMatchCallback {
        fun onSuccess(match: Match)
        fun onFailure(error: Error)
    }
}