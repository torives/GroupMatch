package br.com.yves.groupmatch.presentation.ui.calendar

import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*

object TimeSlotViewModelMapper {
	private const val datePattern = "dd/MM/yyyy"
	private const val hourPattern = "HH:mm"

	fun map(timeSlot: TimeSlot): TimeSlotViewModel {
		timeSlot.apply {
			val dayAndMonth = DateTimeFormatter
					.ofPattern(datePattern)
					.format(start)
			val hour = DateTimeFormatter
					.ofPattern(hourPattern)
					.format(start)
			val weekDay = start.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault())
			val status = if (isBusy) TimeSlotViewModel.Status.Busy else TimeSlotViewModel.Status.Available

			return TimeSlotViewModel(dayAndMonth, weekDay, hour, status)
		}
	}

	fun map(calendarId: Long, timeSlot: TimeSlotViewModel): TimeSlot {
		timeSlot.apply {
			val start = LocalDateTime.of(
					LocalDate.parse(dayAndMonth, DateTimeFormatter.ofPattern(datePattern)),
					LocalTime.parse(hour, DateTimeFormatter.ofPattern(hourPattern))
			)
			val end = start.plusHours(1)
			val isBusy = status == TimeSlotViewModel.Status.Busy

			return TimeSlot(calendarId, start, end, isBusy)
		}
	}
}

