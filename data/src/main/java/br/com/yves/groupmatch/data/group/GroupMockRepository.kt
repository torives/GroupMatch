package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.domain.user.User
import org.threeten.bp.LocalDateTime
class GroupMockRepository : GroupRepository {

	private val members = listOf(
			User("", "Luizinho", ""),
			User("", "Zezinho", ""),
			User("", "Betinho", ""),
			User("", "Boninho", ""),
			User("", "Luluzinha", ""),
			User("", "Magali", "")
	)

	override fun getGroup(groupId: String, callback: GroupRepository.GetGroupCallback) {
		val group = Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now())
		callback.onSuccess(group)
	}


	override fun getAllGroups(userId: String, callback: GroupRepository.GetGroupCallback) {
		val groups = listOf(
				Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("","Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now())
		)
		callback.onSuccess(groups)
	}

	override fun createGroup(group: Group, callback: GroupRepository.CreateGroupCallback) {
		callback.onSuccess("")
	}
}