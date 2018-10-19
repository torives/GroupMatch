package br.com.yves.groupmatch.domain.loadEvents

import org.threeten.bp.LocalDateTime

interface DateRepository{
    fun getCurrentDate(): LocalDateTime
    fun getCurrentWeek(): ClosedRange<LocalDateTime>
}

