package br.com.yves.groupmatch.presentation.ui.groups.create

import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import java.lang.ref.WeakReference

class NewGroupController(
		view: NewGroupView,
		private val userRepository: UserRepository,
		private val groupRepository: GroupRepository,
		private val authenticationService: AuthenticationService,
		private val presenter: UserPresenter
) : UserRepository.GetUsersCallback, GroupRepository.CreateGroupCallback {

	private val selectedUsers = mutableSetOf<UserViewModel>()
	private lateinit var users: List<UserViewModel>
	private val viewRef = WeakReference(view)
	private val view: NewGroupView?
		get() = viewRef.get()

	fun onViewCreated() {
		userRepository.getAllUsers(this)
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
		view?.navigateToNewGroupDetails()
	}

	fun onCreateGroupAttempt(name: String) {
		val loggedUser = authenticationService.getLoggedInUser()!!
		val groupMembers = selectedUsers.map { User("", it.name, "") }.toMutableList()
		groupMembers.add(loggedUser)
		val admins = listOf(loggedUser)

		val newGroup = Group(
				name = name,
				imageURL = "",
				members = groupMembers,
				admins = admins
		)

		groupRepository.createGroup(newGroup, this)
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

	//region GetUsersCallback
	override fun onSuccess(users: List<User>) {
		this.users = users.map { presenter.format(it) }
		view?.displayUsers(this.users)
	}

	override fun onFailure(error: Error) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion

	//region //region GetUsersCallback
	override fun onSuccess() {
		view?.navigateToGroups()
	}

	//TODO: Change error type so error handling is correct
	//endregion

}