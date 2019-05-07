package br.com.yves.groupmatch.presentation.ui.groups

interface GroupsView {
	fun displayGroups(groups: List<GroupViewModel>)
	fun displayLoggedOutLayout()
}