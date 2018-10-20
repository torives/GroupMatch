package br.com.yves.groupmatch.presentation.loadEvents

import br.com.yves.groupmatch.presentation.ui.loadEvents.CalendarPresenter
import br.com.yves.groupmatch.presentation.ui.loadEvents.CalendarPresenterImpl
import br.com.yves.groupmatch.presentation.ui.loadEvents.LoadEventsView

object CalendarPresenterFactory {

    fun create(view: LoadEventsView): CalendarPresenter {
        return CalendarPresenterImpl(view, LoadEventsFactory.create())
    }
}