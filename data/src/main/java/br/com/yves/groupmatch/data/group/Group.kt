package br.com.yves.groupmatch.data.group

import org.threeten.bp.LocalDateTime

class FirebaseGroup {
	var id: String? = null
	var name: String? = null
	var thumbnailURL: String? = null
	var members: List<String>? = null
	var admin: String? = null
	var lastInteractionDate: LocalDateTime? = null
}