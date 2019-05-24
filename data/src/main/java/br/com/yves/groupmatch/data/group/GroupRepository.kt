package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.domain.user.UserRepository
import br.com.yves.groupmatch.domain.group.GroupRepository

class GroupRepository(
		private val firebaseRepository: GroupFirebaseRepository,
		private val userRepository: UserRepository
) : GroupRepository {

	override fun getGroup(groupId: String, callback: GroupRepository.GetGroupCallback) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getAllGroups(userId: String, callback: GroupRepository.GetGroupCallback) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}