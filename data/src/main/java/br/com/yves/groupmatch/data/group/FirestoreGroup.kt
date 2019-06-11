package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.data.match.Match

data class FirestoreGroup(
		val id: String,
		val name: String,
		val image: String,
		val members: List<String>,
		val admins: List<String>,
		val currentMatch: Match? = null
)