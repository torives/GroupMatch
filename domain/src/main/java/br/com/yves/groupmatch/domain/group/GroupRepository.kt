package br.com.yves.groupmatch.domain.group

import br.com.yves.groupmatch.domain.group.Group

interface GroupRepository {
	fun getAllGroups(uid: String): List<Group>
}