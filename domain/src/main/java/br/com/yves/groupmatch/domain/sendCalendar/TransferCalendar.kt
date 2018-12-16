package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import org.threeten.bp.LocalDateTime

data class TransferCalendar(
        val firstDate: LocalDateTime,
        val lastDate: LocalDateTime,
        val busySlots: List<TimeSlot>
)