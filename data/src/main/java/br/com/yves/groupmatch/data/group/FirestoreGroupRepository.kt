package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreGroupRepository(private val userRepository: UserRepository) : GroupRepository {
	private val firestore get() = FirebaseFirestore.getInstance()

	override fun getGroup(groupId: String, callback: GroupRepository.GetGroupCallback) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getAllGroups(userId: String, callback: GroupRepository.GetAllGroupCallback) {
		//TODO: order by last interaction date
		firestore.collection(GROUP_COLLECTION)
				.whereArrayContains(FIELD_MEMBERS, userId)
				.orderBy(FIELD_NAME)
				.get()
				.addOnSuccessListener { query ->
					val groupDocs = query.documents
					val groups = groupDocs.mapNotNull { FirestoreGroupMapper.from(it) }

					handleGetAllGroupsSuccess(groups, callback)

				}.addOnFailureListener {
					//TODO: Corrigir tipo de erro
					callback.onFailure(Error())
				}
	}

	override fun createGroup(group: Group, callback: GroupRepository.CreateGroupCallback) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun handleGetAllGroupsSuccess(groups: List<FirestoreGroup>, callback: GroupRepository.GetAllGroupCallback) {
		val finalGroups = mutableListOf<Group>()
		groups.forEach { group ->
			userRepository.getUsers(group.members, object: UserRepository.GetUsersCallback {
				override fun onSuccess(users: List<User>) {
					val admins = users.filter { group.admins.contains(it.id) }
					val mappedGroup = Group(
							group.id,
							group.name,
							group.image,
							users,
							admins
					)
				}

				override fun onFailure(error: Error) {
					TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
				}

			})
		}
	}

	companion object {
		private const val GROUP_COLLECTION = "groups"
		private const val FIELD_NAME = "name"
		private const val FIELD_MEMBERS = "members"
	}
}

class GroupMapper {
	fun from(firestoreGroup: FirestoreGroup): Group {

	}
}