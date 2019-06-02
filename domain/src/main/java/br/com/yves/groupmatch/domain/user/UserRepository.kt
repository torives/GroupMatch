package br.com.yves.groupmatch.domain.user

import java.lang.Exception

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

open class GroupMatchError(
		val code: Int,
		message: String,
		val exception: Exception? = null
): Error(message, exception)

class APIError(
		code: Int,
		message: String,
		exception: Exception? = null
): GroupMatchError(code, message, exception)

sealed class UserRepositoryError(
		code: Int,
		message: String,
		exception: Exception? = null
): GroupMatchError(code, message, exception) {

	class InexistentUser: UserRepositoryError(1, "User does not exists")
}