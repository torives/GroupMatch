package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.Week
import br.com.yves.groupmatch.domain.showCalendar.TimeSlot
import org.threeten.bp.LocalDateTime

data class BusyCalendar(
        val referenceWeek: Week,
        val busySlots: List<TimeSlot>
)