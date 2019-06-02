package br.com.yves.groupmatch.domain.user

interface UserRepository {
	fun createUser(user: User, callback: CreateUserCallback)
	fun updateUser(user: User, callback: UpdateUserCallback)
	fun userExists(userId: String, callback: UserExistsCallback)

	interface CreateUserCallback {
		fun onSuccess(user: User)
		fun onFailure(error: Error)
	}

	interface UpdateUserCallback {
		fun onSuccess(user: User)
		fun onFailure(error: Error)
	}

	interface UserExistsCallback {
		fun onUserExists()
		fun onUserDoesNotExists()
		fun onFailure(error: Error)
	}
}