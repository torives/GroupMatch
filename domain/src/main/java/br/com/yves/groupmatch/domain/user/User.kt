package br.com.yves.groupmatch.domain.user

data class User(
		val uid: String,
		val name: String,
		val email: String,
		val profileImageURL: String? = null
)