package br.com.yves.groupmatch.domain.sendCalendar

interface CalendarArchiver {
    fun encode(calendar: BusyCalendar): String
    fun decode(data: String): BusyCalendar
}