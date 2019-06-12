package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.data.match.Match
import com.google.firebase.firestore.DocumentSnapshot

object FirestoreGroupMapper {
	fun from(group: FirestoreGroup): Map<String, Any?> {
		return mapOf()
	}

	fun from(document: DocumentSnapshot): FirestoreGroup {
		val data = document.data!!
		val name = data["name"] as String
		val image = data["image"] as String
		val members = data["members"] as? List<String> ?: emptyList()
		val admins = data["admins"] as? List<String> ?: emptyList()
		val currentMatch = data["current_match"] as? Match

		return FirestoreGroup(
				document.id,
				name,
				image,
				members,
				admins,
				currentMatch
		)
	}
}
