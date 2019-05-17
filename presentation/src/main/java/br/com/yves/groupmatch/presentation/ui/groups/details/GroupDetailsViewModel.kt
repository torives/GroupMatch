package br.com.yves.groupmatch.presentation.ui.groups.details

import java.io.Serializable

data class GroupDetailsViewModel(
		val id: String,
		val name: String,
		val imageURL: String
) : Serializable