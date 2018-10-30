package br.com.yves.groupmatch.presentation.ui.showCalendar

import br.com.yves.groupmatch.domain.showCalendar.ShowCalendar
import org.threeten.bp.format.TextStyle
import java.util.*

class ShowCalendarPresenterImpl(private val view: ShowCalendarView,
                                private val showCalendar: ShowCalendar
) : ShowCalendarPresenter {

    override fun onViewCreated() {
        val calendar = showCalendar.execute()
        val days = calendar.timeSlices.groupBy { it.dateTime.toLocalDate() }

        val dayViewModels = days.map { day ->
            val weekName = day.key.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()).toUpperCase()
            val hours = day.value.map {
                HourViewModel(
                    it.dateTime.toLocalTime().toString(),
                    if (it.isBusy) ScheduleStatus.Busy else ScheduleStatus.Available
                )
            }
            DayViewModel(weekName, hours)
        }
        view.showCalendar(CalendarViewModel(dayViewModels))
    }
}

data class CalendarViewModel(val days: List<DayViewModel>)

class DayViewModel(val weekName: String,
                   val hours: List<HourViewModel>
)

class HourViewModel(val label: String,
                    var status: ScheduleStatus
)

enum class ScheduleStatus(val hexColor: String) {
    Available("#b3d044"),
    Busy("#c9232d")
}