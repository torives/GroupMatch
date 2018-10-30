package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.domain.showCalendar.ShowCalendar
import br.com.yves.groupmatch.presentation.factory.loadEvents.LoadEventsFactory

object ShowCalendarFactory {
    fun create(): ShowCalendar {
        return ShowCalendar(
            DateRepositoryFactory.create(),
            LoadEventsFactory.create()
        )
    }
}