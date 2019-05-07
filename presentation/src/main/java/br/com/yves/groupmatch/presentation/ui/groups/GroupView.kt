package br.com.yves.groupmatch.presentation.ui.groups

interface GroupView {
	fun displayGroups(groups: List<GroupViewModel>)
	fun displayLoggedOutLayout()
	fun navigateToGroupDetails(groupId: String)
}