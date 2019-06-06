package br.com.yves.groupmatch.domain.user

interface UserRepository {
	fun getAllUsers(callback: GetAllUsersCallback)
	fun createUser(user: User, callback: CreateUserCallback)
	fun updateUser(user: User)
	fun userExists(userId: String, callback: UserExistsCallback)

	interface GetAllUsersCallback {
		fun onSuccess(users: List<User>)
		fun onFailure(error: Error)
	}

	interface CreateUserCallback {
		fun onSuccess(user: User)
		fun onFailure(error: Error)
	}

	interface UserExistsCallback {
		fun onUserExists()
		fun onUserDoesNotExists()
		fun onFailure(error: Error)
	}
}