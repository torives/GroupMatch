package br.com.yves.groupmatch.data.user

import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import br.com.yves.groupmatch.domain.user.UserRepositoryError
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreUserRepository : UserRepository {
	private val firestore get() = FirebaseFirestore.getInstance()


	override fun getUsers(userIDs: List<String>, callback: UserRepository.GetUsersCallback) {
		firestore.collection(USERS_COLLECTION).get()
				.addOnSuccessListener { query ->
					val userDocs = query.documents
					val users = userDocs.filter { userIDs.contains(it.id) }
							.map { FirestoreUserMapper.from(it) }
					callback.onSuccess(users)
				}
				.addOnFailureListener {
					//TODO: handle error
					callback.onFailure(Error(it))
				}
	}

	override fun getAllUsers(callback: UserRepository.GetUsersCallback) {
		firestore.collection(USERS_COLLECTION).orderBy("name").get()
				.addOnSuccessListener { query ->
					val userDocs = query.documents
					val users = userDocs.mapNotNull { FirestoreUserMapper.from(it) }

					callback.onSuccess(users)
				}.addOnFailureListener {
					//TODO: Corrigir tipo de erro
					callback.onFailure(Error(it))
				}
	}

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
