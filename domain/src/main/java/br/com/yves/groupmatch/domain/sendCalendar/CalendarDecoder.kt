package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.Calendar

interface CalendarDecoder {
    fun decode(data: ByteArray): Calendar
}