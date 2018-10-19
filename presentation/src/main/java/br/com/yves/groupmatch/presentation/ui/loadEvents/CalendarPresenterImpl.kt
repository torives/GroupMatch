package br.com.yves.groupmatch.presentation.ui.loadEvents

import br.com.yves.groupmatch.domain.loadEvents.Event
import br.com.yves.groupmatch.domain.loadEvents.LoadEvents
import br.com.yves.groupmatch.domain.loadEvents.LoadEventsCallback

class CalendarPresenterImpl(private val view: CalendarView,
                            private val loadEvents: LoadEvents
) : CalendarPresenter, LoadEventsCallback {

    init {
        loadEvents.with(this)
    }

    override fun onViewCreated() {
        loadEvents.execute()
    }

    //region LoadEventsCallback
    override fun onSuccess(events: List<Event>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //endregion
}


