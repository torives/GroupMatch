package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.user.UserRepository
import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import java.lang.ref.WeakReference

class GroupController(
		view: GroupView,
		private val presenter: GroupPresenter,
		private val groupRepository: GroupRepository,
		private val userRepository: AuthenticationService
) {

	private val viewWeakReference = WeakReference(view)
	private val view: GroupView?
		get() = viewWeakReference.get()

	fun onViewCreated() {
		userRepository.getLoggedUser()?.uid?.let {
			groupRepository.getAllGroups(it, object : GroupRepository.GetGroupCallback {
				override fun onSuccess(groups: List<Group>) {
					val viewModels = presenter.format(groups)
					view?.displayGroups(viewModels)
				}
				override fun onFailure() {}
			})
		} ?: view?.displayLoggedOutLayout()
	}

	fun onGroupSelected(groupId: String) {
//
//		val repo = GroupFirebaseRepository()
//		val a = repo.getGroup("j6I1aIfMFw4I6rDhn3Yp")
//
//		val group = groupRepository.getGroup(groupId)
//		group?.let {
//			val details = presenter.format(it)
//			view?.navigateToGroupDetails(details)
//		} ?: throw InvalidGroupException(groupId)
	}

	fun onGroupCreationAttempt() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}