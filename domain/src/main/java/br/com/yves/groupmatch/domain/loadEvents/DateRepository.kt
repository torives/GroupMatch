package br.com.yves.groupmatch.domain.loadEvents

import org.threeten.bp.LocalDateTime

interface DateRepository{
    fun getCurrentWeek(): ClosedRange<LocalDateTime>
}

