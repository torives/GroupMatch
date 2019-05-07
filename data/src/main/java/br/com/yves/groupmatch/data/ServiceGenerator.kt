package br.com.yves.groupmatch.data

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
	private val gsonFactory by lazy {
		val gson = GsonBuilder().create()
		GsonConverterFactory.create(gson)
	}

	private val httpClient by lazy { OkHttpClient.Builder().build() }

	fun <S> createService(serviceClass: Class<S>, baseUrl: String, interceptor: Interceptor? = null): S {
		var client = httpClient

		interceptor?.let {
			client = httpClient.newBuilder()
					.addInterceptor(it)
					.build()
		}

		val builder = Retrofit.Builder()
				.addConverterFactory(gsonFactory)
				.baseUrl(baseUrl)
				.client(client)

		return builder.build().create(serviceClass)
	}
}