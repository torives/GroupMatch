package br.com.yves.groupmatch.data.db.calendar

import androidx.room.*
import br.com.yves.groupmatch.data.db.week.WeekRoom
import br.com.yves.groupmatch.domain.models.Week

@Dao
interface CalendarRoomDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun add(calendar: CalendarRoom)

	@Update
	fun update(calendar: CalendarRoom)

	@Delete
	fun delete(calendar: CalendarRoom)

	@Query("""
		SELECT *
		FROM ${CalendarRoom.TABLE_NAME}
		WHERE ${WeekRoom.COLUMN_START} == :week.start
		AND ${WeekRoom.COLUMN_END} == :week.end
	""")
	fun getCalendar(week: Week): CalendarRoom?

	@Query("SELECT * FROM ${CalendarRoom.TABLE_NAME} WHERE ${CalendarRoom.COLUMN_ID} == :id")
	fun getCalendar(id: Int): CalendarRoom?
}