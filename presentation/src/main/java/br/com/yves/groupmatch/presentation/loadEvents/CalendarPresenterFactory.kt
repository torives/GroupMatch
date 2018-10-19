package br.com.yves.groupmatch.presentation.loadEvents

import br.com.yves.groupmatch.presentation.ui.loadEvents.CalendarPresenter
import br.com.yves.groupmatch.presentation.ui.loadEvents.CalendarPresenterImpl
import br.com.yves.groupmatch.presentation.ui.loadEvents.CalendarView

object CalendarPresenterFactory {

    fun create(view: CalendarView): CalendarPresenter {
        return CalendarPresenterImpl(view, LoadEventsFactory.create())
    }
}