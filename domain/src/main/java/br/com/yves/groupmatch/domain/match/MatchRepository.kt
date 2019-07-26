package br.com.yves.groupmatch.domain.match

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.user.User

interface MatchRepository {
    fun getMatch(matchId: String, callback: GetMatchCallback)
    fun getMatches(matchIds: List<String>, callback: GetMatchesCallback)
    fun startMatch(group: Group, creator: User, localCalendar: Calendar, callback: StartMatchCallback)
    fun sendAnswer(matchId: String, user: User, localCalendar: Calendar, callback: SendAnswerCallback)

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

    interface SendAnswerCallback {
        fun onSuccess()
        fun onFailure(error: Error)
    }
}
