package br.com.yves.groupmatch.data.db.timeSlot

import androidx.room.*

@Dao
interface CalendarTimeSlotRoomDAO {

	@Query("""
		SELECT *
		FROM ${CalendarTimeSlotRoom.TABLE_NAME}
		WHERE ${CalendarTimeSlotRoom.COLUMN_CALENDAR_ID} == :calendarId
	""")
	fun getTimeSlotsFromCalendar(calendarId: Long): List<CalendarTimeSlotRoom>?

	@Insert(onConflict = OnConflictStrategy.FAIL)
	fun insert(calendarTimeSlot: CalendarTimeSlotRoom)

	//region Delete
	@Delete
	fun delete(calendarTimeSlot: CalendarTimeSlotRoom)

	@Query(value = "DELETE FROM ${CalendarTimeSlotRoom.TABLE_NAME}")
	fun clear()
	//endregion

	@Update
	fun update(calendarTimeSlot: CalendarTimeSlotRoom)
}