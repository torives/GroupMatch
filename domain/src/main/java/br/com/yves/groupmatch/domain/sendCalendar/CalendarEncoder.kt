package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.Calendar

interface CalendarEncoder {
    fun encode(calendar: Calendar): ByteArray
}

