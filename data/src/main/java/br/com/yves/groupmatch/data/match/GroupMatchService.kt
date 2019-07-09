package br.com.yves.groupmatch.data.match

import retrofit2.Call
import retrofit2.http.*

interface GroupMatchService {
	@POST("matches")
	fun startMatch(@Body match: RemoteMatch): Call<Response>

	@POST("matches/{matchId}/answers")
	fun sendAnswer(@Path("matchId") matchId: String, @Body answer: FirestoreAnswer): Call<Response>

	data class Response(
			var success: Boolean,
			var message: String
	)
}