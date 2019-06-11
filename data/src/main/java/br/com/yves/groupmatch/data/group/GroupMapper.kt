package br.com.yves.groupmatch.data.group

import br.com.yves.groupmatch.domain.group.Group

object GroupMapper {
    fun from(group: Group): Map<String, Any?> {
        return mapOf(
                "name" to group.name,
                "image" to group.imageURL,
                "members" to group.members.map { it.id },
                "admins" to group.admins.map { it.id }
        )
    }
}