package br.com.yves.groupmatch.presentation.ui.group.create

import br.com.yves.groupmatch.presentation.ui.group.create.details.NewGroupDetailsViewModel

interface NewGroupView {
	fun displayUsers(users: List<UserViewModel>)
	fun displayNextButton()
	fun hideNextButton()
	fun navigateToNewGroupDetails(viewModel: NewGroupDetailsViewModel)
}