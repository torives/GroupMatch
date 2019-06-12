package br.com.yves.groupmatch.domain.group

import br.com.yves.groupmatch.domain.match.Match
import br.com.yves.groupmatch.domain.user.User

data class Group(
		val id: String = "",
		val name: String,
		val imageURL: String,
		val members: List<User>,
		val admins: List<User>,
		val match: Match? = null
)