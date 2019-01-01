package br.com.yves.groupmatch.domain.sendCalendar

interface CalendarArchiver {
    fun encode(calendar: ClientCalendar): ByteArray
    fun decode(data: ByteArray): ClientCalendar
}