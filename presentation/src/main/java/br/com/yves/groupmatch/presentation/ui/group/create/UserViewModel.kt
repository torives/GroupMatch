package br.com.yves.groupmatch.presentation.ui.group.create

import java.io.Serializable

data class UserViewModel(
		val name: String,
		val email: String,
		var isSelected: Boolean,
		val profileImageURL: String? = null
): Serializable