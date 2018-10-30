package br.com.yves.groupmatch.domain.showCalendar

import org.threeten.bp.LocalDateTime

typealias Week = ClosedRange<LocalDateTime>

interface DateRepository{
    fun getCurrentWeek(): Week
    fun getAllDatesFrom(week: Week): List<LocalDateTime>
}

