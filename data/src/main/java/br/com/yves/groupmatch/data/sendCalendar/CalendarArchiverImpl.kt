package br.com.yves.groupmatch.data.sendCalendar

import br.com.yves.groupmatch.domain.sendCalendar.CalendarArchiver
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendar
import com.google.gson.Gson


class CalendarArchiverImpl: CalendarArchiver {
    private val gson by lazy { Gson() }

    override fun encode(calendar: BusyCalendar): String {
       return gson.toJson(calendar)
    }

    override fun decode(data: String): BusyCalendar {
        return gson.fromJson(data, BusyCalendar::class.java)
    }
}