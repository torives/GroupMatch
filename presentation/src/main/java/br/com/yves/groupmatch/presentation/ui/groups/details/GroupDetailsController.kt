package br.com.yves.groupmatch.presentation.ui.groups.details

import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.presentation.ui.groups.InvalidGroupException
import java.lang.ref.WeakReference

class GroupDetailsController(
		private val groupId: String,
		view: GroupDetailView,
		private val presenter: GroupDetailsPresenter,
		private val repository: GroupRepository
) {

	private val viewWeakReference = WeakReference(view)
	private val view: GroupDetailView?
		get() = viewWeakReference.get()

	fun onViewCreated() {
		repository.getGroup(groupId)?.let { group ->
			val members = presenter.format(group.members)
			view?.displayGroupMembers(members)
		} ?: throw InvalidGroupException(groupId)
	}

	fun onMatchSelected() {
		TODO()
	}

	fun onLeaveGroupAttempt(){
		TODO()
	}
}