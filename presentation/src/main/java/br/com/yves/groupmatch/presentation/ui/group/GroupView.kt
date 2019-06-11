package br.com.yves.groupmatch.presentation.ui.group

import br.com.yves.groupmatch.presentation.ui.group.details.GroupDetailsViewModel

interface GroupView {
	fun displayGroups(groups: List<GroupViewModel>)
	fun displayLoggedOutLayout()
	fun navigateToGroupDetails(details: GroupDetailsViewModel)
	fun navigateToNewGroup()
}