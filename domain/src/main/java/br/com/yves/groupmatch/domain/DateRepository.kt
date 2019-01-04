package br.com.yves.groupmatch.domain

import org.threeten.bp.LocalDateTime

interface DateRepository {
	fun getCurrentWeek(): Week
	fun getAllDatesFrom(week: Week): List<LocalDateTime>
}

