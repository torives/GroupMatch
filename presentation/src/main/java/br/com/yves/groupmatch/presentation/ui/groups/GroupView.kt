package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.presentation.ui.bluetooth.server.MatchResultViewModel
import br.com.yves.groupmatch.presentation.ui.groups.details.GroupDetailsViewModel

interface GroupView {
	fun displayGroups(groups: List<GroupViewModel>)
	fun displayLoggedOutLayout()
	fun displayDialog(title: String, message: String, onPositiveResponse: () -> Unit, onNegativeResponse: () -> Unit)
	fun navigateToGroupDetails(details: GroupDetailsViewModel)
	fun navigateToNewGroup()
	fun navigateToMatchResult(result: MatchResultViewModel)
}