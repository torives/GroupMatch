package br.com.yves.groupmatch.presentation.injection

import br.com.yves.groupmatch.data.auth.GroupMatchAuth
import br.com.yves.groupmatch.data.group.FirestoreGroupRepository
import br.com.yves.groupmatch.data.group.GroupMockRepository
import br.com.yves.groupmatch.data.match.FirestoreMatchRepository
import br.com.yves.groupmatch.data.user.FirestoreUserRepository
import br.com.yves.groupmatch.presentation.ui.groups.GroupController
import br.com.yves.groupmatch.presentation.ui.groups.GroupFragment
import br.com.yves.groupmatch.presentation.ui.groups.GroupPresenterImpl

class GroupInjection {
	fun make(view: GroupFragment) = GroupController(
			view,
			GroupPresenterImpl(),
			FirestoreGroupRepository(FirestoreUserRepository(), FirestoreMatchRepository()),
			GroupMatchAuth.instance
	)
}