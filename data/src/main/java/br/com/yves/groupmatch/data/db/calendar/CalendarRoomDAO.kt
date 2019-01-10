package br.com.yves.groupmatch.data.db.calendar

import androidx.room.*
import br.com.yves.groupmatch.domain.Week

@Dao
interface CalendarRoomDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun add(calendar: CalendarRoom)

	@Update
	fun update(calendar: CalendarRoom)

	@Delete
	fun delete(calendar: CalendarRoom)

	@Query("SELECT * FROM ${CalendarRoom.TABLE_NAME} WHERE ${CalendarRoom.COLUMN_WEEK} == :week")
	fun getCalendar(week: Week): List<CalendarRoom>
}