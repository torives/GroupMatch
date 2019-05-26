package br.com.yves.groupmatch.presentation.injection

import br.com.yves.groupmatch.data.auth.GroupMatchAuth
import br.com.yves.groupmatch.data.group.GroupMockRepository
import br.com.yves.groupmatch.presentation.ui.groups.GroupController
import br.com.yves.groupmatch.presentation.ui.groups.GroupFragment
import br.com.yves.groupmatch.presentation.ui.groups.GroupPresenterImpl

class GroupInjection {
	fun make(view: GroupFragment) = GroupController(
			view,
			GroupPresenterImpl(),
			GroupMockRepository(),
			GroupMatchAuth.instance
	)
}