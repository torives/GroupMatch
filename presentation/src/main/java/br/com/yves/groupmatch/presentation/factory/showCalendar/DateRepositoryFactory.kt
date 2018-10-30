package br.com.yves.groupmatch.presentation.factory.showCalendar

import br.com.yves.groupmatch.data.loadEvents.DateRepositoryImpl
import br.com.yves.groupmatch.domain.showCalendar.DateRepository

object DateRepositoryFactory {
    fun create(): DateRepository {
        return DateRepositoryImpl()
    }
}