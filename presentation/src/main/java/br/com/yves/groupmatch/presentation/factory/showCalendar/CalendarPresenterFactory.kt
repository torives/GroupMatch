package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.presentation.ui.showCalendar.ShowCalendarPresenter
import br.com.yves.groupmatch.presentation.ui.showCalendar.ShowCalendarView

object CalendarPresenterFactory {
	fun create(view: ShowCalendarView) = ShowCalendarPresenter(view, ShowCalendarFactory.create())
}

