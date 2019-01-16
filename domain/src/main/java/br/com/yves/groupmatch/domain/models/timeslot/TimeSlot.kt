package br.com.yves.groupmatch.domain.models.timeslot

import org.threeten.bp.LocalDateTime

interface TimeSlot {
	val start: LocalDateTime
	val end: LocalDateTime
	val isBusy: Boolean
}


