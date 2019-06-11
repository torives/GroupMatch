package br.com.yves.groupmatch.presentation.ui.group.details

import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.presentation.ui.account.UserViewModel

interface GroupDetailsPresenter {
	fun format(users: List<User>): List<UserViewModel>
}