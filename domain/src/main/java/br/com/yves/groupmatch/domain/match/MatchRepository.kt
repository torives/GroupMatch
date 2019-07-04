package br.com.yves.groupmatch.domain.match

interface MatchRepository {
    fun getMatch(matchId: String, callback: GetMatchCallback)
    fun getMatches(matchIds: List<String>, callback: GetMatchesCallback)

    interface GetMatchCallback {
        fun onSuccess(match: Match)
        fun onFailure(error: Error)
    }

    interface GetMatchesCallback {
        fun onSuccess(matches: List<Match>)
        fun onFailure(error: Error)
    }
}