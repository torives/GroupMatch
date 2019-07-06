package br.com.yves.groupmatch.data.match

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupMatchService {
    @POST("matches")
    fun startMatch(@Body match: RemoteMatch): Call<Response>

    data class Response(
            var success: Boolean,
            var message: String
    )
}