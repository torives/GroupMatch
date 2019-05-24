package br.com.yves.groupmatch.presentation.ui.groups.details

import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.presentation.ui.account.UserViewModel

class GroupDetailsPresenterImpl: GroupDetailsPresenter {
	override fun format(users: List<User>): List<UserViewModel> {
		return users.map { UserViewModel(it.name, it.email, it.profileImageURL) }
	}

}