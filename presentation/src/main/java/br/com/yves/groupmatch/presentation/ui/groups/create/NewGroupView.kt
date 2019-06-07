package br.com.yves.groupmatch.presentation.ui.groups.create

interface NewGroupView {
	fun displayUsers(users: List<UserViewModel>)
	fun displayNextButton()
	fun hideNextButton()
	fun navigateToNewGroupDetails()
}