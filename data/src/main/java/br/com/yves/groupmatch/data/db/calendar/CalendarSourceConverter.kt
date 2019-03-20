package br.com.yves.groupmatch.data.db.calendar

import androidx.room.TypeConverter
import br.com.yves.groupmatch.domain.models.calendar.Calendar

class CalendarSourceConverter {
	@TypeConverter
	fun toSource(source: Int): Calendar.Source {
		return when (source) {
			Calendar.Source.LOCAL.ordinal -> Calendar.Source.LOCAL
			Calendar.Source.REMOTE.ordinal -> Calendar.Source.REMOTE
			else -> throw IllegalArgumentException("Could not convert Calendar.Source")
		}
	}

	@TypeConverter
	fun toInteger(status: Calendar.Source): Int {
		return status.ordinal
	}
}