package br.com.yves.groupmatch.data.sendCalendar

import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.sendCalendar.CalendarDecoder
import br.com.yves.groupmatch.domain.sendCalendar.CalendarEncoder
import com.google.gson.Gson


class CalendarBinaryMapper : CalendarEncoder, CalendarDecoder {
	private val gson by lazy { Gson() }

	override fun encode(calendar: Calendar): ByteArray {
		return gson.toJson(calendar).toByteArray()
	}

	override fun decode(data: ByteArray): Calendar {
		val message = String(data)
		return gson.fromJson(message, Calendar::class.java)
	}
}