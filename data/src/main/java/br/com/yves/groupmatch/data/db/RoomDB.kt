package br.com.yves.groupmatch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom

@Database(
	entities = [TimeSlotRoom::class],
	version = 3
)
@TypeConverters(RoomTypeConverter::class)
abstract class RoomDB : RoomDatabase() {

	abstract fun timeSlotDAO(): TimeSlotRoomDAO

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


