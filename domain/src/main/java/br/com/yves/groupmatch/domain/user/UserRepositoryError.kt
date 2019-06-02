package br.com.yves.groupmatch.domain.user

import br.com.yves.groupmatch.domain.GroupMatchError

sealed class UserRepositoryError{
	class InexistentUser(exception: Exception): GroupMatchError(1, "User does not exists", exception)
	class UserCreationFailed(exception: Exception): GroupMatchError(2, "Failed to create user", exception)
	class UserExistenceCheckFailed(exception: Exception): GroupMatchError(3, "Failed to check if user exists", exception)
}