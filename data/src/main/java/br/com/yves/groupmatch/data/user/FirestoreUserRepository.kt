package br.com.yves.groupmatch.data.user

import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreUserRepository : UserRepository {
	private val firestore get() = FirebaseFirestore.getInstance()

	override fun createUser(user: User, callback: UserRepository.CreateUserCallback) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun updateUser(user: User, callback: UserRepository.UpdateUserCallback) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun userExists(userId: String, callback: UserRepository.UserExistsCallback) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	companion object {
		private const val USERS_COLLECTION = "users"
	}
}