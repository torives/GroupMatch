package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.domain.models.account.User
import org.threeten.bp.LocalDateTime

class GroupMockRepository : GroupRepository {
	override fun getAllGroups(uid: String): List<Group> {
		val members = listOf(
				User("", "Luizinho", ""),
				User("", "Zezinho", ""),
				User("", "Betinho", ""),
				User("", "Boninho", ""),
				User("", "Luluzinha", ""),
				User("", "Magali", "")
		)
		return listOf(
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now()),
				Group("Nome do Grupo", "https://i.redd.it/4fz0ct0l7mo11.jpg", members, members.first(), LocalDateTime.now())
		)
	}
}