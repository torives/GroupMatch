package br.com.yves.groupmatch.domain

import br.com.yves.groupmatch.domain.models.Week
import org.threeten.bp.LocalDateTime

interface DateRepository {
	fun getCurrentWeek(): Week
	fun getAllDatesFrom(week: Week): List<LocalDateTime>
}

