package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.models.calendar.Calendar

interface CalendarEncoder {
    fun encode(calendar: Calendar): Any
}

