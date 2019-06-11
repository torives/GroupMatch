package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreGroupRepository(private val userRepository: UserRepository) : GroupRepository {
    private val firestore get() = FirebaseFirestore.getInstance()

    override fun getGroup(groupId: String, callback: GroupRepository.GetGroupCallback) {
        firestore.collection(GROUP_COLLECTION).document(groupId).get()
                .addOnSuccessListener { document ->
                    val group = FirestoreGroupMapper.from(document)
                    handleGetGroupSuccess(group, callback)
                }.addOnFailureListener {
                    callback.onFailure(Error())
                }
    }

    override fun getAllGroups(userId: String, callback: GroupRepository.GetAllGroupCallback) {
        firestore.collection(GROUP_COLLECTION)
                .whereArrayContains(FIELD_MEMBERS, userId)
                .orderBy(FIELD_NAME) //TODO: order by last interaction date
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
        val groupData = GroupMapper.from(group)
        firestore.collection(GROUP_COLLECTION).document().set(groupData)
                .addOnSuccessListener {
                    callback.onSuccess()
                }
                .addOnFailureListener {
                    //TODO: Corrigir tipo de erro
                    callback.onFailure(Error())
                }
    }

    private fun handleGetAllGroupsSuccess(groups: List<FirestoreGroup>, callback: GroupRepository.GetAllGroupCallback) {
        val userIds = groups.flatMap { it.members }
        userRepository.getUsers(userIds, object : UserRepository.GetUsersCallback {

            override fun onSuccess(users: List<User>) {
                val newGroups = groups.map { group ->
                    val members = users.filter { group.members.contains(it.id) }
                    val admins = users.filter { group.admins.contains(it.id) }
                    Group(
                            group.id,
                            group.name,
                            group.image,
                            members,
                            admins
                    )
                }
                callback.onSuccess(newGroups)
            }

            override fun onFailure(error: Error) {
                callback.onFailure(error)
            }
        })
    }

    private fun handleGetGroupSuccess(firestoreGroup: FirestoreGroup, callback: GroupRepository.GetGroupCallback) {
        userRepository.getUsers(firestoreGroup.members, object : UserRepository.GetUsersCallback {
            override fun onSuccess(users: List<User>) {
                val admins = users.filter { firestoreGroup.admins.contains(it.id) }
                val group = Group(
                        firestoreGroup.id,
                        firestoreGroup.name,
                        firestoreGroup.image,
                        users,
                        admins
                )
                callback.onSuccess(group)
            }

            override fun onFailure(error: Error) {
                callback.onFailure(error)
            }
        })
    }

    companion object {
        private const val GROUP_COLLECTION = "groups"
        private const val FIELD_NAME = "name"
        private const val FIELD_MEMBERS = "members"
    }
}

