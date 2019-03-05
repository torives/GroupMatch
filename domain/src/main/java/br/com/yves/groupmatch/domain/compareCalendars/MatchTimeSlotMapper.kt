package br.com.yves.groupmatch.domain.compareCalendars

import br.com.yves.groupmatch.domain.models.slots.TimeSlot

object MatchTimeSlotMapper {
	fun map(slot: TimeSlot): TimeSlot = TimeSlot(
			slot.start,
			slot.end,
			slot.isBusy
	)
}