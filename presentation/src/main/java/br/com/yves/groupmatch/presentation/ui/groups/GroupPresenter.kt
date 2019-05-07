package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.domain.group.Group

interface GroupPresenter {
	fun format(groups: List<Group>): List<GroupViewModel>
}