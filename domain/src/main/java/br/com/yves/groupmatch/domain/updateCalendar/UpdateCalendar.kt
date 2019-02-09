package br.com.yves.groupmatch.domain.updateCalendar

import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.models.timeslot.TimeSlot

class UpdateCalendar(private val repository: CalendarRepository) : UseCase<Unit>() {

    private lateinit var timeSlot: TimeSlot

    fun with(timeSlot: TimeSlot): UpdateCalendar {
        this.timeSlot = timeSlot
        return this
    }

    override fun execute() {
        this.timeSlot.isBusy = this.timeSlot.isBusy.not()
        repository.getCalendar(timeSlot.calendarId)?.apply {
            val index = timeSlots.indexOfFirst { it.start == timeSlot.start && it.end == timeSlot.end }
            if (index >= 0) {
                timeSlots[index] = timeSlot
                repository.update(this)
            } else {
                //log this bitch
            }
        }
    }
}