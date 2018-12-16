package br.com.yves.groupmatch.domain.sendCalendar

interface CalendarArchiver {
    fun encode(calendar: TransferCalendar): String
    fun decode(data: String): TransferCalendar
}