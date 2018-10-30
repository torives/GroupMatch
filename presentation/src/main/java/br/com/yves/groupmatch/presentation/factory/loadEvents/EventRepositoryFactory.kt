package br.com.yves.groupmatch.presentation.factory.loadEvents

import br.com.yves.groupmatch.data.loadEvents.EventRepositoryImpl
import br.com.yves.groupmatch.domain.loadEvents.EventRepository
import br.com.yves.groupmatch.presentation.Application

object EventRepositoryFactory {

    fun create(): EventRepository {
        return EventRepositoryImpl(Application.INSTANCE)
    }
}