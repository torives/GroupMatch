package br.com.yves.groupmatch.domain.createCalendar

import br.com.yves.groupmatch.domain.DateRepository

object CreateCalendarFactory {
	fun create(dateRepository: DateRepository) =
		CreateCalendar(dateRepository)
}
