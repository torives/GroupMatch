package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.domain.match.Match

data class GroupViewModel(
		val id: String,
		val name: String,
		val members: String,
		val imageURL: String,
		val matchStatus: Match.Status?,
		val lasInteractionDate: String
)