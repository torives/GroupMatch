package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.domain.group.Group

class GroupPresenterImpl : GroupPresenter {
	override fun format(groups: List<Group>): List<GroupViewModel> {
		return groups.map { group ->
			val members: String = group.members.map { it.name }.reduce { acc, s ->
				if (acc.isEmpty()) {
					s
				} else {
					"$acc, $s"
				}
			}
			GroupViewModel(
					group.id,
					group.name,
					members,
					group.thumbnailURL,
					false,
					""
			)
		}
	}
}