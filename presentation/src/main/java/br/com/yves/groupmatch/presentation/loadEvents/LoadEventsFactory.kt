package br.com.yves.groupmatch.presentation.loadEvents

import br.com.yves.groupmatch.domain.loadEvents.LoadEvents

object LoadEventsFactory {

    fun create(): LoadEvents =
            LoadEvents(EventRepositoryFactory.create(),
                    DateRepositoryFactory.create())
}

