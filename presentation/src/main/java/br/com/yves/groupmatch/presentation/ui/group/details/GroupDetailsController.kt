package br.com.yves.groupmatch.presentation.ui.group.details

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
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
		repository.getGroup(groupId, object : GroupRepository.GetGroupCallback {

			override fun onSuccess(group: Group) {
				val members = presenter.format(group.members)
				view?.displayGroupMembers(members)
			}

			override fun onFailure(error: Error) {
				TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
			}
		})
	}

	fun onMatchSelected() {
		TODO()
	}

	fun onLeaveGroupAttempt() {
		TODO()
	}
}
