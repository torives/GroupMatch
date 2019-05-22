package br.com.yves.groupmatch.data.group

import android.util.Log
import br.com.yves.groupmatch.domain.group.Group
import br.com.yves.groupmatch.domain.group.GroupRepository
import br.com.yves.groupmatch.domain.models.account.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GroupFirestoreRepository : GroupRepository {

	private val db: FirebaseFirestore
		get() = FirebaseFirestore.getInstance()

	//region GroupRepository
	override fun getGroup(groupId: String): Group? = runBlocking {
		return@runBlocking withContext(Dispatchers.IO) {
			getGroupSynchronous(groupId)
		}
	}

	override fun getAllGroups(userId: String): List<Group> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion

	private suspend fun getGroupSynchronous(groupId: String): Group? = suspendCoroutine { cont ->
		db.collection(COLLECTION_GROUPS).document(groupId).get()
				.addOnSuccessListener {
					val group = it.toObject(FirestoreGroup::class.java)
//					cont.resume(Group("", "","", emptyList(), User(), )
				}
				.addOnFailureListener {
					Log.d(TAG, "Failed to retrieve group with id: $groupId")
					cont.resume(null)
				}
	}

	companion object {
		private val TAG = GroupFirestoreRepository::class.java.simpleName
		private const val COLLECTION_GROUPS = "groups"
		private const val COLLECTION_GROUPS_ID = "id"
	}
}