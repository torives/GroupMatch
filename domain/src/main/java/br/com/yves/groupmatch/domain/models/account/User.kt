package br.com.yves.groupmatch.domain.models.account

data class User(
		val name: String,
		val email: String,
		val profileImageURL: String?
)