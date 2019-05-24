package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.data.group.GroupFirebaseRepository
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.group.GroupRepository
import java.lang.ref.WeakReference

class GroupController(
		view: GroupView,
		private val presenter: GroupPresenter,
		private val groupRepository: GroupRepository,
		private val authenticationService: AuthenticationService
) {
	private val viewWeakReference = WeakReference(view)
	private val view: GroupView?
		get() = viewWeakReference.get()

	fun onViewCreated() {
		authenticationService.getUser()?.uid?.let {
			val groups = groupRepository.getAllGroups(it)
			val viewModels = presenter.format(groups)

			view?.displayGroups(viewModels)
		} ?: view?.displayLoggedOutLayout()
	}

	fun onGroupSelected(groupId: String) {

		val repo = GroupFirebaseRepository()
		val a = repo.getGroup("j6I1aIfMFw4I6rDhn3Yp")

		val group = groupRepository.getGroup(groupId)
		group?.let {
			val details = presenter.format(it)
			view?.navigateToGroupDetails(details)
		} ?: throw InvalidGroupException(groupId)
	}

	fun onGroupCreationAttempt() {}
}