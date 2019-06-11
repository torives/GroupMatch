package br.com.yves.groupmatch.presentation.injection

import br.com.yves.groupmatch.data.auth.GroupMatchAuth
import br.com.yves.groupmatch.data.group.GroupMockRepository
import br.com.yves.groupmatch.presentation.ui.group.GroupController
import br.com.yves.groupmatch.presentation.ui.group.GroupFragment
import br.com.yves.groupmatch.presentation.ui.group.GroupPresenterImpl

class GroupInjection {
	fun make(view: GroupFragment) = GroupController(
			view,
			GroupPresenterImpl(),
			GroupMockRepository(),
			GroupMatchAuth.instance
	)
}