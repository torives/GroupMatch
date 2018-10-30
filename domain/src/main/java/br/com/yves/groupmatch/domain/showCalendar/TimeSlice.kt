package br.com.yves.groupmatch.domain.showCalendar

import org.threeten.bp.LocalDateTime

data class TimeSlice(val dateTime: LocalDateTime,
                     val isBusy: Boolean
)