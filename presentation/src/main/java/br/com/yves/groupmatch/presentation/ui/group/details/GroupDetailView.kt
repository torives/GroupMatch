package br.com.yves.groupmatch.presentation.ui.group.details

import br.com.yves.groupmatch.presentation.ui.account.UserViewModel

interface GroupDetailView {
	fun displayMatchButton()
	fun hideMatchButton()
	fun displayGroupMembers(members: List<UserViewModel>)
}