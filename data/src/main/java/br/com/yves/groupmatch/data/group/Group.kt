package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.domain.models.account.User
import org.threeten.bp.LocalDateTime

class FirestoreGroup {
	val id: String? = null
	val name: String? = null
	val thumbnailURL: String? = null
	val members: List<User>? = null
	val admin: User? = null
	val lastInteractionDate: LocalDateTime? = null
}