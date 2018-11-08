package br.com.yves.groupmatch.data.db

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class RoomTypeConverter {
	private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

	@TypeConverter
	fun fromTimestamp(value: String?): LocalDateTime? {
		return value?.let {
			formatter.parse(it, LocalDateTime::from)
		}
	}

	@TypeConverter
	fun dateToTimestamp(date: LocalDateTime?): String? {
		return date?.format(formatter)
	}
}