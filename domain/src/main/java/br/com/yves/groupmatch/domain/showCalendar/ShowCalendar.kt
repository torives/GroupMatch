package br.com.yves.groupmatch.domain.showCalendar

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.loadEvents.LoadEvents

class ShowCalendar(private val dateRepository: DateRepository,
                   private val loadEvents: LoadEvents
) : UseCase<Calendar>() {

    override fun execute(): Calendar {
        val currentWeek = dateRepository.getCurrentWeek()
        val calendar = dateRepository.getAllDatesFrom(currentWeek)
        val events = loadEvents.from(currentWeek).execute()
        val timeSlices = mutableListOf<TimeSlice>()

        for (dateTime in calendar) {
            val isBusy = events.find { it.date == dateTime } != null
            timeSlices.add(TimeSlice(dateTime, isBusy))
        }
        return Calendar(timeSlices)
    }
}

