package br.com.yves.groupmatch.data.sendCalendar

import br.com.yves.groupmatch.domain.sendCalendar.CalendarArchiver
import br.com.yves.groupmatch.domain.sendCalendar.ClientCalendar
import com.google.gson.Gson


class ClientCalendarArchiverImpl : CalendarArchiver {
	private val gson by lazy { Gson() }

	override fun encode(calendar: ClientCalendar): ByteArray {
		return gson.toJson(calendar).toByteArray()
	}

	override fun decode(data: ByteArray): ClientCalendar {
		val message = String(data)
		return gson.fromJson(message, ClientCalendar::class.java)
	}
}