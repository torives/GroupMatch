package br.com.yves.groupmatch.data.user

import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import br.com.yves.groupmatch.domain.user.UserRepositoryError
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreUserRepository : UserRepository {
	private val firestore get() = FirebaseFirestore.getInstance()

	override fun createUser(user: User, callback: UserRepository.CreateUserCallback) {
		val userData = FirestoreUserMapper.from(user)
		firestore.collection(USERS_COLLECTION).document(user.id).set(userData)
				.addOnSuccessListener {
					callback.onSuccess(user)
				}.addOnFailureListener {
					callback.onFailure(UserRepositoryError.UserCreationFailed(it))
				}
	}

	override fun updateUser(user: User) {
		firestore.collection(USERS_COLLECTION).document(user.id).set(user, SetOptions.merge())
	}

	override fun userExists(userId: String, callback: UserRepository.UserExistsCallback) {
		firestore.collection(USERS_COLLECTION).document(userId).get()
				.addOnSuccessListener {
					if (it.exists()) {
						callback.onUserExists()
					} else {
						callback.onUserDoesNotExists()
					}
				}
				.addOnFailureListener {
					callback.onFailure(UserRepositoryError.UserExistenceCheckFailed(it))
				}
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