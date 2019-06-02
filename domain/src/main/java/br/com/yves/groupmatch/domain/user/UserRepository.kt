package br.com.yves.groupmatch.domain.user

interface UserRepository {
	fun getUser(uid: String, callback: GetUserCallback)
	fun updateUser(user: User, callback: UpdateUserCallback)

	interface GetUserCallback {
		fun onSuccess(user: User)
		fun onFailure(error: Error)
	}

	interface UpdateUserCallback {
		fun onSuccess()
		fun onFailure(error: Error)
	}
}