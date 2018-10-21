package br.com.yves.groupmatch.data.db.event

import androidx.room.*
import org.threeten.bp.LocalDateTime

@Dao
interface EventRoomDAO {
    //region Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(event: EventRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(events: List<EventRoom>)
    //endregion

    //region Delete
    @Delete
    fun delete(event: EventRoom)

    @Delete
    fun delete(events: List<EventRoom>)
    //endregion

    @Query("""
        SELECT * FROM ${EventRoom.TABLE_NAME}
        WHERE ${EventRoom.COLUMN_DATE} >= :firstDate
        AND ${EventRoom.COLUMN_DATE} <= :lastDate""")
    fun getAllEventsBetween(firstDate: String, lastDate: String): List<EventRoom>
}