package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.group.GroupRepository

class GroupController(
		private val view: GroupView,
		private val presenter: GroupPresenter,
		private val groupRepository: GroupRepository,
		private val authenticationService: AuthenticationService
) {

	fun onViewCreated() {
		authenticationService.getUser()?.uid?.let {
			val groups = groupRepository.getAllGroups(it)
			val viewModels = presenter.format(groups)

			view.displayGroups(viewModels)
		} ?: view.displayLoggedOutLayout()
	}

	fun onGroupSelected(groupId: String) {
		val group = groupRepository.getGroup(groupId)
		group?.let {
			val details = presenter.format(it)
			view.navigateToGroupDetails(details)
		} ?: throw IllegalStateException("Group with id: $groupId does not exist")
	}

	fun onGroupCreationAttempt() {}
}