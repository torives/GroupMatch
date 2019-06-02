package br.com.yves.groupmatch.data.user

import br.com.yves.groupmatch.domain.user.APIError
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreUserRepository : UserRepository {
	private val firestore get() = FirebaseFirestore.getInstance()

	override fun createUser(user: User, callback: UserRepository.CreateUserCallback) {
		val userData = FirestoreUserMapper.from(user)
		firestore.collection(USERS_COLLECTION).document(user.id).set(userData)
				.addOnSuccessListener {
					callback.onSuccess(user)
				}.addOnFailureListener {
					callback.onFailure(APIError(
							500,
							"Failed to create user",
							it)
					)
				}
	}

	override fun updateUser(user: User, callback: UserRepository.UpdateUserCallback) {
		firestore.collection(USERS_COLLECTION).document(user.id).set(user)
	}

	override fun userExists(userId: String, callback: UserRepository.UserExistsCallback) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	companion object {
		private const val USERS_COLLECTION = "users"
	}
}

object FirestoreUserMapper {
	fun from(user: User): Map<String, Any?> {
		return mapOf(
				"name" to user.name,
				"email" to user.email,
				"profileImage" to user.profileImageURL,
				"tokens" to user.tokens?.mapKeys { it.key.name }
		)
	}
}