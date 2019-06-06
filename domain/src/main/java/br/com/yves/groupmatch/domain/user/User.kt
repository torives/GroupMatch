package br.com.yves.groupmatch.domain.user

data class User(
		var id: String = "",
		var name: String = "",
		var email: String = "",
		var profileImageURL: String? = "",
		var tokens: MutableMap<Tokens, String> = mutableMapOf()
)

enum class Tokens {
	auth, access, refresh, device
}