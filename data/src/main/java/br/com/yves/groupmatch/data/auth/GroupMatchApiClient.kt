package br.com.yves.groupmatch.data.auth

import android.content.Context
import android.util.Log
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.ServiceGenerator
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupMatchApiClient(applicationContext: Context) {

	private val service by lazy {
		ServiceGenerator.createService(
				GroupMatchAPI::class.java,
				applicationContext.getString(R.string.groupmatch_api_baseURL)
		)
	}

	fun registerAuthorizationToken(token: String) {
		val token= mapOf("token" to token)
		service.registerAuthorizationToken(token).enqueue(object : Callback<AuthTokenResponse> {
			override fun onResponse(call: Call<AuthTokenResponse>, response: Response<AuthTokenResponse>) {
				Log.d(TAG, "Response is successful: ${response.isSuccessful}")
			}

			override fun onFailure(call: Call<AuthTokenResponse>, t: Throwable) {
				Log.e(TAG, "Failed to register authentication token", t)
			}
		})
	}

	companion object {
		private val TAG = GroupMatchApiClient::class.java.simpleName
	}
}