package br.com.yves.groupmatch.presentation.ui.groups.create

import br.com.yves.groupmatch.domain.user.User

class UserPresenterImpl : UserPresenter {
	override fun format(user: User) = UserViewModel(
			user.name,
			user.email,
			false,
			user.profileImageURL
	)
}