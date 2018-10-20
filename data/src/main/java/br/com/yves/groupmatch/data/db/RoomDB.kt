package br.com.yves.groupmatch.data.db

import android.content.Context
import androidx.room.*
import br.com.yves.groupmatch.data.db.EventRoom.Companion.COLUMN_ID
import br.com.yves.groupmatch.data.db.EventRoom.Companion.TABLE_NAME
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import br.com.yves.groupmatch.data.R

@Database(
        entities = [EventRoom::class],
        version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun eventDAO(): EventDAO

    companion object {
        private var INSTANCE: RoomDB? = null

        fun getInstance(): RoomDB {
            return INSTANCE ?: throw IllegalStateException("Must initialize RoomDB on Application")
        }

        fun init(applicationContext: Context) {
            INSTANCE = Room.databaseBuilder(
                    applicationContext,
                    RoomDB::class.java,
                    applicationContext.getString(R.string.database_name)
            ).build()
        }
    }
}


@Dao
interface EventDAO {
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
}

@Entity(tableName = TABLE_NAME,
        indices = [Index(COLUMN_ID, unique = true)])
data class EventRoom(
        @ColumnInfo(name = COLUMN_ID) @PrimaryKey val id: Int,
        val date: LocalDateTime
) {
    companion object {
        const val TABLE_NAME = "events"
        const val COLUMN_ID = "id"
    }
}

class RoomTypeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            formatter.parse(it, LocalDateTime::from)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}