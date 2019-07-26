package br.com.yves.groupmatch.presentation.ui.groups.create

import br.com.yves.groupmatch.domain.user.User

interface UserPresenter {
	fun format(user: User): UserViewModel
}