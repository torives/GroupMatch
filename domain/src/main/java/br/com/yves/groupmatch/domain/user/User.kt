package br.com.yves.groupmatch.domain.user

data class User(
		val id: String,
		val name: String,
		val email: String,
		val profileImageURL: String? = null,
		val tokens: MutableMap<Tokens, String> = mutableMapOf()
)

enum class Tokens {
	auth, access, refresh, device
}