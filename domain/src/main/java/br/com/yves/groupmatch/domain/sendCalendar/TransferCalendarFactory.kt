package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.showCalendar.Calendar

object TransferCalendarFactory {
    fun create(calendar: Calendar): TransferCalendar {
        val firstDate = calendar.first().date
        val lastDate = calendar.last().date
        val busySlots = calendar.filter { it.isBusy }

        return TransferCalendar(firstDate.rangeTo(lastDate), busySlots)
    }
}