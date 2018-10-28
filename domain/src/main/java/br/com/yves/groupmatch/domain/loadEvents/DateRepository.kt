package br.com.yves.groupmatch.domain.loadEvents

import org.threeten.bp.LocalDateTime

typealias Week = ClosedRange<LocalDateTime>

interface DateRepository{
    fun getCurrentWeek(): Week
    fun getAllHoursFrom(week: Week): List<LocalDateTime>
}

