package br.com.yves.groupmatch.data.sendCalendar

import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.sendCalendar.CalendarDecoder
import br.com.yves.groupmatch.domain.sendCalendar.CalendarEncoder
import com.google.gson.*
import java.lang.reflect.Type


class CalendarCodec : JsonSerializer<Calendar> {
	override fun serialize(src: Calendar?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return JsonObject().apply {
			val jsonTimeSlots = JsonArray()
			src?.calendarTimeSlots?.forEach { timeSlot ->
				if (timeSlot.isBusy) {
					val jsonSlot = JsonObject().apply {
						addProperty("start", timeSlot.start.toString())
						addProperty("end", timeSlot.end.toString())
					}
					jsonTimeSlots.add(jsonSlot)
				}
			}
			add("busySlots", jsonTimeSlots)
		}
	}
}

class CalendarJsonMapper : CalendarEncoder, CalendarDecoder {
	private val gson by lazy {
		GsonBuilder().run {
			registerTypeAdapter(Calendar::class.java, CalendarCodec())
			create()
		}
	}

	override fun encode(calendar: Calendar): String {
		return gson.toJson(calendar)
	}

	override fun decode(data: Any): Calendar? {
		val dataString = data as? String
		return gson.fromJson(dataString, Calendar::class.java)
	}
}