package br.com.yves.groupmatch.data.auth

import br.com.yves.groupmatch.domain.models.account.User
import com.google.firebase.auth.FirebaseUser

object FirebaseUserMapper {
	fun from(user: FirebaseUser?): User? {
		return if (user?.displayName != null && user.email != null) {
			User(
					user.displayName!!,
					user.email!!,
					user.photoUrl?.toString()
			)
		} else {
			null
		}
	}
}