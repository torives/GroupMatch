package br.com.yves.groupmatch.data.auth

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupMatchAPI {

	@POST("auth/token")
	fun registerAuthorizationToken(@Body token: Map<String, String>): Call<AuthTokenResponse>
}


//data class Token(val token: String)

data class AuthTokenResponse(
		val error: Error?
)

data class Error(
		val code: Int,
		val message: String
)