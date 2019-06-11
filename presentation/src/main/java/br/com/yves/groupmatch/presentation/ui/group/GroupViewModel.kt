package br.com.yves.groupmatch.presentation.ui.group

data class GroupViewModel(
		val id: String,
		val name: String,
		val members: String,
		val imageURL: String,
		val hasUpdates: Boolean,
		val lasInteractionDate: String
)