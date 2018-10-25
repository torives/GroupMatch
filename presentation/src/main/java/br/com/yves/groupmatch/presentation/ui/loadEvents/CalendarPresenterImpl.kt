package br.com.yves.groupmatch.presentation.ui.loadEvents

import br.com.yves.groupmatch.domain.loadEvents.LoadEvents

class CalendarPresenterImpl(private val view: LoadEventsView,
                            private val loadEvents: LoadEvents
) : CalendarPresenter {

    override fun onViewCreated() {
        val events = loadEvents.execute()
//        view.showEvents()
    }
}


