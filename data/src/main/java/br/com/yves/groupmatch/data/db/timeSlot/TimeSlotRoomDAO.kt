package br.com.yves.groupmatch.data.db.timeSlot

import androidx.room.*

@Dao
interface TimeSlotRoomDAO {

	@Query("""
		SELECT *
		FROM ${TimeSlotRoom.TABLE_NAME}
		WHERE ${TimeSlotRoom.COLUMN_CALENDAR_ID} == :calendarId
	""")
	fun getTimeSlotsFromCalendar(calendarId: Long): List<TimeSlotRoom>?

	@Insert(onConflict = OnConflictStrategy.FAIL)
	fun insert(timeSlot: TimeSlotRoom)

	//region Delete
	@Delete
	fun delete(timeSlot: TimeSlotRoom)

	@Query(value = "DELETE FROM ${TimeSlotRoom.TABLE_NAME}")
	fun clear()
	//endregion

	@Update
	fun update(timeSlot: TimeSlotRoom)
}