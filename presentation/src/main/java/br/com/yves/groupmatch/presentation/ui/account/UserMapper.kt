package br.com.yves.groupmatch.presentation.ui.account

import br.com.yves.groupmatch.domain.user.User

object UserMapper {
	fun from(user: User) = UserViewModel(
			user.name,
			user.email,
			user.profileImageURL
	)
}