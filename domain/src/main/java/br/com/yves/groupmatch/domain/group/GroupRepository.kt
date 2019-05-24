package br.com.yves.groupmatch.domain.group

interface GroupRepository {
	fun getGroup(groupId: String, callback: GetGroupCallback)
	fun getAllGroups(userId: String, callback: GetGroupCallback)

	interface GetGroupCallback {
		fun onSuccess(group: Group){}
		fun onSuccess(groups: List<Group>){}
		fun onFailure()
	}
}