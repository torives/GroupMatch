package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.showCalendar.Calendar

object BusyCalendarFactory {
    fun create(calendar: Calendar): BusyCalendar {
        val firstDate = calendar.first().date
        val lastDate = calendar.last().date
        val busySlots = calendar.filter { it.isBusy }

        return BusyCalendar(firstDate.rangeTo(lastDate), busySlots)
    }
}