package br.com.yves.groupmatch.domain.updateCalendar

import br.com.yves.groupmatch.domain.CalendarRepository
import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.models.slots.CalendarTimeSlot

class UpdateCalendar(private val repository: CalendarRepository) : UseCase<Unit>() {

    private lateinit var calendarTimeSlot: CalendarTimeSlot

    fun with(calendarTimeSlot: CalendarTimeSlot): UpdateCalendar {
        this.calendarTimeSlot = calendarTimeSlot
        return this
    }

    override fun execute() {
        this.calendarTimeSlot.isBusy = this.calendarTimeSlot.isBusy.not()
        repository.getCalendar(calendarTimeSlot.calendarId)?.apply {
            val index = calendarTimeSlots.indexOfFirst { it.start == calendarTimeSlot.start && it.end == calendarTimeSlot.end }
            if (index >= 0) {
                calendarTimeSlots[index] = calendarTimeSlot
                repository.update(this)
            } else {
                //log this bitch
            }
        }
    }
}