package br.com.yves.groupmatch.domain.match

interface MatchRepository {
    fun getMatch(matchId: String, callback: GetMatchCallback)
    fun getMatches(matchIds: List<String>, callback: GetMatchesCallback)
    fun startMatch(groupId: String, callback: StartMatchCallback)

    interface GetMatchCallback {
        fun onSuccess(match: Match)
        fun onFailure(error: Error)
    }

    interface GetMatchesCallback {
        fun onSuccess(matches: List<Match>)
        fun onFailure(error: Error)
    }

    interface StartMatchCallback {
        fun onSuccess()
        fun onFailure(error: Error)
    }
}
