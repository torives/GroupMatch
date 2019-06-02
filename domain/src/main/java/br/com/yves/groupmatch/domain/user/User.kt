package br.com.yves.groupmatch.domain.user

data class User(
		val id: String,
		val name: String,
		val email: String,
		val profileImageURL: String? = null,
		val tokens: Map<Tokens, String>? = null
)

enum class Tokens {
	ACCESS, REFRESH, DEVICE
}