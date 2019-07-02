package br.com.yves.groupmatch.data.group

data class FirestoreGroup(
        val id: String,
        val name: String,
        val image: String,
        val members: List<String>,
        val admins: List<String>,
        val currentMatchId: String? = null
)