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

	override fun decode(data: Any): Calendar? {
		return when (data) {
			is ByteArray -> {
				val message = String(data)
				gson.fromJson(message, Calendar::class.java)
			}
			else -> null
		}
	}
}