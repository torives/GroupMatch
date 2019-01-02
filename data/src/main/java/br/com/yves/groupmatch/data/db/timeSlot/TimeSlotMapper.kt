package br.com.yves.groupmatch.data.db.timeSlot

import br.com.yves.groupmatch.domain.loadCalendar.TimeSlot
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

object TimeSlotMapper {

	fun from(timeSlot: TimeSlot) = TimeSlotRoom(timeSlot.date, timeSlot.isBusy)

	fun from(timeSlot: TimeSlotRoom) = TimeSlot(timeSlot.date, timeSlot.isBusy)

	fun from(day: String, hour: String, isBusy: Boolean) = TimeSlot(
		LocalDateTime.of(
			LocalDate.parse(day, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
			LocalTime.parse(hour, DateTimeFormatter.ofPattern("HH:mm"))
		), isBusy
	)
}