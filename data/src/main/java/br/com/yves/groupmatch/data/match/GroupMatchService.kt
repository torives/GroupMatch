package br.com.yves.groupmatch.data.match

import retrofit2.Call
import retrofit2.http.POST

interface GroupMatchService {
    @POST("matches/{match_id}/lineup")
    fun startMatch(match: RemoteMatch): Call<Response>

    data class Response(
            var success: Boolean,
            var message: String
    )
}