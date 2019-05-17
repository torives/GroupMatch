package br.com.yves.groupmatch.domain.group

interface GroupRepository {
	fun getGroup(groupId: String): Group?
	fun getAllGroups(userId: String): List<Group>
}