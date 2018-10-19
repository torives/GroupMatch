package br.com.yves.groupmatch.presentation.loadEvents

import br.com.yves.groupmatch.data.loadEvents.DateRepositoryImpl
import br.com.yves.groupmatch.domain.loadEvents.DateRepository

object DateRepositoryFactory {
    fun create(): DateRepository {
        return DateRepositoryImpl()
    }
}