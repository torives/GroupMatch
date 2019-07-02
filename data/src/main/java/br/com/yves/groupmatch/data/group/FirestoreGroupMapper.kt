package br.com.yves.groupmatch.data.group

import com.google.firebase.firestore.DocumentSnapshot

object FirestoreGroupMapper {
    fun from(document: DocumentSnapshot): FirestoreGroup {
        val data = document.data!!
        val name = data["name"] as String
        val image = data["image"] as String
        val members = data["members"] as? List<String> ?: emptyList()
        val admins = data["admins"] as? List<String> ?: emptyList()
        val currentMatchId = data["current_match"] as? String

        return FirestoreGroup(
                document.id,
                name,
                image,
                members,
                admins,
                currentMatchId
        )
    }
}
