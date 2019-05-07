package br.com.yves.groupmatch.presentation.ui.account

data class UserViewModel(
		val name: String,
		val email: String,
		val profileImageURL: String? = null
)