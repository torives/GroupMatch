package br.com.yves.groupmatch.data.db.timeSlot

import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlotImpl
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

object TimeSlotMapper {
	private const val datePattern = "dd/MM/yyyy"
	private const val hourPattern = "HH:mm"

	fun from(timeSlot: TimeSlotImpl, calendarID: Long): TimeSlotRoom =
		TimeSlotRoom(calendarID, timeSlot.start, timeSlot.end, timeSlot.isBusy)


	fun from(day: String, hour: String, isBusy: Boolean): TimeSlot {
		val start = LocalDateTime.of(
				LocalDate.parse(day, DateTimeFormatter.ofPattern(datePattern)),
				LocalTime.parse(hour, DateTimeFormatter.ofPattern(hourPattern))
		)
		val end = start.plusHours(1)

		return TimeSlotImpl(start, end, isBusy)
	}
}

