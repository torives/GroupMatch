package br.com.yves.groupmatch.data.user

import br.com.yves.groupmatch.domain.user.Tokens
import br.com.yves.groupmatch.domain.user.User
import com.google.firebase.firestore.DocumentSnapshot

object FirestoreUserMapper {
	fun from(user: User): Map<String, Any?> {
		return mapOf(
				"name" to user.name,
				"email" to user.email,
				"profileImage" to user.profileImageURL,
				"tokens" to user.tokens?.mapKeys { it.key.name }
		)
	}

	fun from(user: DocumentSnapshot): User {
		val data = user.data!!
		val name = data["name"] as String
		val email = data["email"] as String
		val profileImage = data["profileImage"] as String
		val tokens = (data["tokens"] as MutableMap<String, String>)
				.mapKeys { Tokens.valueOf(it.key) }
				.toMutableMap()

		return User(
				user.id,
				name,
				email,
				profileImage,
				tokens
		)
	}
}