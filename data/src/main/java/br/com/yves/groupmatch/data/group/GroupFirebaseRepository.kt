package br.com.yves.groupmatch.data.group

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GroupFirebaseRepository {

	private val db: FirebaseFirestore
		get() = FirebaseFirestore.getInstance()

	//region GroupRepository
	suspend fun getGroup(groupId: String): FirebaseGroup? = suspendCoroutine { cont ->
		db.collection(COLLECTION_GROUPS).document(groupId).get()
				.addOnSuccessListener {
					val group = it.toObject(FirebaseGroup::class.java)
					cont.resume(group)
				}
				.addOnFailureListener {
					Log.d(TAG, "Failed to retrieve group with id: $groupId")
					cont.resume(null)
				}
	}

	fun getAllGroups(userId: String): List<FirebaseGroup> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion

	private suspend fun getGroupSynchronous(groupId: String): FirebaseGroup? = suspendCoroutine { cont ->
		db.collection(COLLECTION_GROUPS).document(groupId).get()
				.addOnSuccessListener {
					val group = it.toObject(FirebaseGroup::class.java)
					cont.resume(group)
				}
				.addOnFailureListener {
					Log.d(TAG, "Failed to retrieve group with id: $groupId")
					cont.resume(null)
				}
	}

	companion object {
		private val TAG = GroupFirebaseRepository::class.java.simpleName
		private const val COLLECTION_GROUPS = "groups"
		private const val COLLECTION_GROUPS_ID = "id"
	}
}