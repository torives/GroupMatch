package br.com.yves.groupmatch.domain.models.account

data class User(
		val uid: String,
		val name: String,
		val email: String,
		val profileImageURL: String? = null
)