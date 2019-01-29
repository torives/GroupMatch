package br.com.yves.groupmatch.data.db.timeSlot

import androidx.room.*

@Dao
interface TimeSlotRoomDAO {

	//region Select
	@Query("SELECT * FROM ${TimeSlotRoom.TABLE_NAME}")
	fun getAllTimeSlots(): List<TimeSlotRoom>

	@Query("""
		SELECT *
		FROM ${TimeSlotRoom.TABLE_NAME}
		WHERE ${TimeSlotRoom.COLUMN_CALENDAR_ID} == :calendarId
	""")
	fun getAllTimeSlotsFromCalendar(calendarId: Long): List<TimeSlotRoom>?
	//endregion

	//region Insert
	@Insert(onConflict = OnConflictStrategy.FAIL)
	fun insert(timeSlot: TimeSlotRoom)
	//endregion

    //region Delete
	@Delete
    fun delete(timeSlot: TimeSlotRoom)

	@Query(value = "DELETE FROM ${TimeSlotRoom.TABLE_NAME}")
	fun clear()
	//endregion

	@Update
	fun update(timeSlot: TimeSlotRoom)

	@Query(
		"""
        SELECT * FROM ${TimeSlotRoom.TABLE_NAME}
        WHERE ${TimeSlotRoom.COLUMN_START} >= :firstDate
        AND ${TimeSlotRoom.COLUMN_END} <= :lastDate
    """
	)
	fun getAllTimeSlotsBetween(firstDate: String, lastDate: String): List<TimeSlotRoom>
}