package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository

class GroupFirestoreRepository: GroupRepository {

	//region GroupRepository
	override fun getGroup(groupId: String): Group? {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getAllGroups(userId: String): List<Group> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion
}