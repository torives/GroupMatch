package br.com.yves.groupmatch.domain.match

import br.com.yves.groupmatch.domain.models.slots.TimeSlot

data class Match(
        val id: String,
        val status: Status,
        val groupId: String,
        val result: List<TimeSlot>?
) {
    enum class Status {
        CREATED, ONGOING, FINISHED;
    }
}