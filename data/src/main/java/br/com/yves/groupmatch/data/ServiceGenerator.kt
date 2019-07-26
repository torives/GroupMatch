package br.com.yves.groupmatch.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
	private val gsonFactory by lazy {
		val gson = GsonBuilder().create()
		GsonConverterFactory.create(gson)
	}

	private val httpClient by lazy { OkHttpClient.Builder().build() }

	fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {

		val builder = Retrofit.Builder()
				.addConverterFactory(gsonFactory)
				.baseUrl(baseUrl)
				.client(httpClient)

		return builder.build().create(serviceClass)
	}
}