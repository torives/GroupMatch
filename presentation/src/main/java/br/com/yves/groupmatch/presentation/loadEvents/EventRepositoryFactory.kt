package br.com.yves.groupmatch.presentation.loadEvents

import br.com.yves.groupmatch.data.loadEvents.EventRepositoryImpl
import br.com.yves.groupmatch.domain.loadEvents.EventRepository

object EventRepositoryFactory {

    fun create(): EventRepository {
        return EventRepositoryImpl()
    }
}