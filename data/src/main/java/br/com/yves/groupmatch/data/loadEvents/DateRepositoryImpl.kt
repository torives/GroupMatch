package br.com.yves.groupmatch.data.loadEvents

import br.com.yves.groupmatch.domain.showCalendar.DateRepository
import br.com.yves.groupmatch.domain.showCalendar.Week
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
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

    override fun getAllDatesFrom(week: Week): List<LocalDateTime> {
        val weekTimeSlots = mutableListOf<LocalDateTime>()
        var currentDay = week.start

        while (!currentDay.toLocalDate().isAfter(week.endInclusive.toLocalDate())) {
            val currentDayTimeSlots = getAllHoursFrom(currentDay)

            weekTimeSlots.addAll(currentDayTimeSlots)
            currentDay = currentDay.plusDays(1)
        }

        return weekTimeSlots
    }

    //FIXME: Considerar as diferentes TimeZones
    private fun getCurrentDay(): LocalDateTime {
        val zoneId = ZoneId.of("America/Sao_Paulo")
        return LocalDateTime.now(zoneId)
    }

    private fun getAllHoursFrom(day: LocalDateTime): List<LocalDateTime> {
        val nextDay = day.plusDays(1)
        var currentDateTime = day
        val timeSlots = mutableListOf<LocalDateTime>()

        while (currentDateTime.isBefore(nextDay)) {
            timeSlots.add(currentDateTime)
            currentDateTime = currentDateTime.plusHours(1)
        }
        return timeSlots
    }
}




