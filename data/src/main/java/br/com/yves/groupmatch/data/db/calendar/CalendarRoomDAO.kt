package br.com.yves.groupmatch.data.db.calendar

import androidx.room.*
import org.threeten.bp.LocalDateTime

@Dao
interface CalendarRoomDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(calendar: CalendarRoom): Long

	@Update
	fun update(calendar: CalendarRoom)

	@Query("""
		DELETE
		FROM ${CalendarRoom.TABLE_NAME}
		WHERE ${CalendarRoom.COLUMN_ID} == :calendarId
	""")
	fun delete(calendarId: Long)

	@Query("""
		SELECT *
		FROM ${CalendarRoom.TABLE_NAME}
		WHERE ${CalendarRoom.COLUMN_INITIAL_DATE} == :start
		AND ${CalendarRoom.COLUMN_FINAL_DATE} == :end
	""")
	fun getCalendar(start: LocalDateTime, end: LocalDateTime): CalendarRoom?

	@Query("SELECT * FROM ${CalendarRoom.TABLE_NAME} WHERE ${CalendarRoom.COLUMN_ID} == :id")
	fun getCalendar(id: Long): CalendarRoom?
}