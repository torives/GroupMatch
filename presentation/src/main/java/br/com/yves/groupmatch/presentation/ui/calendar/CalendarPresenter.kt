package br.com.yves.groupmatch.presentation.ui.calendar

import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotMapper
import br.com.yves.groupmatch.domain.Calendar
import br.com.yves.groupmatch.domain.loadCalendar.LoadCalendar
import br.com.yves.groupmatch.domain.updateCalendar.UpdateCalendar
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*

class CalendarPresenter(
		private val view: CalendarView,
		private val loadCalendar: LoadCalendar,
		private val updateCalendar: UpdateCalendar
) {
	//private lateinit var calendar: CalendarRoom

	fun onViewCreated() {
		showCalendar()
	}

	private fun showCalendar() {
		val calendar = loadCalendar.execute()
		val calendarViewModel = createCalendarViewModel(calendar)

		view.showCalendar(calendarViewModel)
	}

	private fun createCalendarViewModel(calendar: Calendar): CalendarViewModel {
		val days = calendar.timeSlots.groupBy { it.start.toLocalDate() }

		val dayViewModels = days.map { day ->
			val dayAndMonth = day.key.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
			val weekDay = day.key.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault())
					.toUpperCase()
			val hours = day.value.map {
				HourViewModel(
						it.start.toLocalTime().toString(),
						if (it.isBusy) ScheduleStatus.Busy else ScheduleStatus.Available
				)
			}
			DayViewModel(dayAndMonth, weekDay, hours)
		}
		return CalendarViewModel(dayViewModels)
	}

	fun onTimeSlotClicked(day: DayViewModel, hour: HourViewModel) {
		val isBusy = hour.status == ScheduleStatus.Busy
		updateCalendar.with(
				listOf(TimeSlotMapper.from(
						day.dayAndMonth,
						hour.label,
						isBusy
				))
		).execute()
		showCalendar()
	}
}

data class CalendarViewModel(val days: List<DayViewModel>)

class DayViewModel(
		val dayAndMonth: String,
		val weekDay: String,
		val hours: List<HourViewModel>
)

class HourViewModel(
		val label: String,
		var status: ScheduleStatus
)

enum class ScheduleStatus(val hexColor: String) {
	Available("#b3d044"),
	Busy("#c9232d")
}