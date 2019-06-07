package br.com.yves.groupmatch.presentation.ui.groups.create

import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import java.lang.ref.WeakReference

class NewGroupController(
		view: NewGroupView,
		private val repository: UserRepository,
		private val presenter: UserPresenter
) : UserRepository.GetAllUsersCallback {

	private val selectedUsers = mutableSetOf<UserViewModel>()
	private lateinit var users: List<UserViewModel>
	private val viewRef = WeakReference(view)
	private val view: NewGroupView?
		get() = viewRef.get()

	fun onViewCreated() {
		repository.getAllUsers(this)
	}

	fun onUserSelected(user: UserViewModel) {
		if (user.isSelected) {
			deselect(user)
		} else {
			select(user)
		}
		view?.displayUsers(this.users)
	}

	fun onNextButtonClick() {
		view?.navigateToNewGroupDetails(NewGroupDetailsViewModel(selectedUsers.toList()))
	}

	private fun select(user: UserViewModel) {
		user.isSelected = true

		selectedUsers.add(user)
		view?.displayNextButton()
	}

	private fun deselect(user: UserViewModel) {
		user.isSelected = false
		selectedUsers.remove(user)

		if (selectedUsers.isEmpty()) {
			view?.hideNextButton()
		}
	}

	//region GetAllUsersCallback
	override fun onSuccess(users: List<User>) {
		this.users = users.map { presenter.format(it) }
		view?.displayUsers(this.users)
	}

	override fun onFailure(error: Error) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion
}