package br.com.yves.groupmatch.presentation.ui.group

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.presentation.ui.group.details.GroupDetailsViewModel

interface GroupPresenter {
	fun format(group: Group): GroupDetailsViewModel
	fun format(groups: List<Group>): List<GroupViewModel>
}