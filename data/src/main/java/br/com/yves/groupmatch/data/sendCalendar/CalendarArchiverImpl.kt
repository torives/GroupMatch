package br.com.yves.groupmatch.data.sendCalendar

import br.com.yves.groupmatch.domain.sendCalendar.CalendarArchiver
import br.com.yves.groupmatch.domain.sendCalendar.TransferCalendar
import com.google.gson.Gson


class CalendarArchiverImpl: CalendarArchiver {
    private val gson by lazy { Gson() }

    override fun encode(calendar: TransferCalendar): String {
       return gson.toJson(calendar)
    }

    override fun decode(data: String): TransferCalendar {
        return gson.fromJson(data, TransferCalendar::class.java)
    }
}