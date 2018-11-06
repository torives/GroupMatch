package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.presentation.factory.updateCalendar.UpdateCalendarFactory
import br.com.yves.groupmatch.presentation.ui.calendar.CalendarPresenter
import br.com.yves.groupmatch.presentation.ui.calendar.CalendarView

object CalendarPresenterFactory {
	fun create(view: CalendarView) =
		CalendarPresenter(view, ShowCalendarFactory.create(), UpdateCalendarFactory.create())
}

