package br.com.yves.groupmatch.presentation.ui.groups.details

import br.com.yves.groupmatch.domain.models.account.User
import br.com.yves.groupmatch.presentation.ui.account.UserViewModel

interface GroupDetailsPresenter {
	fun format(users: List<User>): List<UserViewModel>
}