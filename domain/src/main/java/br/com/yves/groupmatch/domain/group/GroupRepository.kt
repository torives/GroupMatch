package br.com.yves.groupmatch.domain.group

import java.lang.Error

interface GroupRepository {
	fun getGroup(groupId: String, callback: GetGroupCallback)
	fun getAllGroups(userId: String, callback: GetAllGroupCallback)
	fun createGroup(group: Group, callback: CreateGroupCallback)

	interface GetGroupCallback {
		fun onSuccess(group: Group){}
		fun onFailure(error: Error)
	}

	interface GetAllGroupCallback {
		fun onSuccess(groups: List<Group>){}
		fun onFailure(error: Error)
	}

	interface CreateGroupCallback {
		fun onSuccess(groupId: String){}
		fun onFailure(error: Error)
	}
}