package br.com.yves.groupmatch.domain.group

import br.com.yves.groupmatch.domain.models.account.User
import org.threeten.bp.LocalDateTime

data class Group(
		val name: String,
		val thumbnailURL: String,
		val members: List<User>,
		val admin: User,
		val lastInteractionDate: LocalDateTime
)