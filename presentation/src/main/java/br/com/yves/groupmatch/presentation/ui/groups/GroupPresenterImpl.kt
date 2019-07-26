package br.com.yves.groupmatch.presentation.ui.groups

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.presentation.ui.groups.details.GroupDetailsViewModel

class GroupPresenterImpl : GroupPresenter {
	override fun format(group: Group): GroupDetailsViewModel {
		return GroupDetailsViewModel(
				group.id,
				group.name,
				group.imageURL
		)
	}

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
					group.imageURL,
					group.match?.status,
					""
			)
		}
	}
}