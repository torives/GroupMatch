package br.com.yves.groupmatch.data.loadEvents

import br.com.yves.groupmatch.domain.loadEvents.DateRepository
import org.threeten.bp.LocalDateTime

class DateRepositoryImpl : DateRepository {
    override fun getCurrentDate(): LocalDateTime {
        return LocalDateTime.now()
    }

    override fun getCurrentWeek(): ClosedRange<LocalDateTime> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}