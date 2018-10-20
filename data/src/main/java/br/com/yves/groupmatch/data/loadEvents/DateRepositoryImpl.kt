package br.com.yves.groupmatch.data.loadEvents

import br.com.yves.groupmatch.domain.loadEvents.DateRepository
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.TemporalAccessor
import org.threeten.bp.temporal.TemporalAdjusters

class DateRepositoryImpl : DateRepository {

    override fun getCurrentWeek(): ClosedRange<LocalDateTime> {
        val today = getCurrentDay()
        var firstWeekDay = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))

        var lastWeekDay = when(today.dayOfWeek) {
            DayOfWeek.MONDAY -> {
                firstWeekDay = today
                today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
            }
            DayOfWeek.SUNDAY -> today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
            else -> today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
        }

        firstWeekDay = firstWeekDay.with(LocalTime.MIN)
        lastWeekDay = lastWeekDay.with(LocalTime.MAX)
        return firstWeekDay.rangeTo(lastWeekDay)
    }

    private fun getCurrentDay(): LocalDateTime {
        val zoneId = ZoneId.of("America/Sao_Paulo")
        return LocalDateTime.now(zoneId)
    }
}




