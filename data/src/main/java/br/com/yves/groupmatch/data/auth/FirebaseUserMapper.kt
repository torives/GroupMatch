package br.com.yves.groupmatch.data.auth

import br.com.yves.groupmatch.domain.user.Tokens
import br.com.yves.groupmatch.domain.user.User
import com.google.firebase.auth.FirebaseUser

object FirebaseUserMapper {
	fun from(user: FirebaseUser?, authToken: String? = null): User? {
		return if (user?.displayName != null && user.email != null) {
			User(
					user.uid,
					user.displayName!!,
					user.email!!,
					user.photoUrl?.toString(),
					authToken?.let { mapOf(Tokens.AUTH to authToken) }
			)
		} else {
			null
		}
	}
}