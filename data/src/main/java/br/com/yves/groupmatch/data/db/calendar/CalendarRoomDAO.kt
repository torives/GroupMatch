package br.com.yves.groupmatch.data.db.calendar

import androidx.room.*
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom
import br.com.yves.groupmatch.data.db.week.WeekRoom
import org.threeten.bp.LocalDateTime

@Dao
interface CalendarRoomDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(calendar: CalendarRoom): Long

	@Update
	fun update(calendar: CalendarRoom)

	@Delete
	fun delete(calendar: CalendarRoom)

	@Query("""
		SELECT *
		FROM ${CalendarRoom.TABLE_NAME}
		INNER JOIN ${TimeSlotRoom.TABLE_NAME}
		ON ${CalendarRoom.COLUMN_ID} == ${TimeSlotRoom.COLUMN_CALENDAR_ID}
		WHERE ${WeekRoom.COLUMN_START} == :start
		AND ${WeekRoom.COLUMN_END} == :end
	""")
	fun getCalendar(start: LocalDateTime, end: LocalDateTime): CalendarRoom? //TODO: resolver map do JOIN para lista de TimeSlots

	@Query("SELECT * FROM ${CalendarRoom.TABLE_NAME} WHERE ${CalendarRoom.COLUMN_ID} == :id")
	fun getCalendar(id: Int): CalendarRoom?
}