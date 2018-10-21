package br.com.yves.groupmatch.data.db

import android.content.Context
import androidx.room.*
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.event.EventRoomDAO
import br.com.yves.groupmatch.data.db.event.EventRoom

@Database(
        entities = [EventRoom::class],
        version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun eventDAO(): EventRoomDAO

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


