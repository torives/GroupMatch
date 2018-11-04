package br.com.yves.groupmatch.data.db.timeSlot

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimeSlotRoomDAO {
	//region Insert
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertOrReplace(timeSlot: TimeSlotRoom)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertOrReplace(events: List<TimeSlotRoom>)
	//endregion

//    //region Delete
//    @Query("""
//        DELETE FROM ${TimeSlotRoom.TABLE_NAME}
//        WHERE ${TimeSlotRoom.COLUMN_ID} == :eventId
//    """)
//    fun delete(eventId: Int)

	@Query(value = "DELETE FROM ${TimeSlotRoom.TABLE_NAME}")
	fun clear()
	//endregion

	@Query(
		"""
        SELECT * FROM ${TimeSlotRoom.TABLE_NAME}
        WHERE ${TimeSlotRoom.COLUMN_DATE} >= :firstDate
        AND ${TimeSlotRoom.COLUMN_DATE} <= :lastDate
    """
	)
	fun getAllTimeSlotsBetween(firstDate: String, lastDate: String): List<TimeSlotRoom>
}