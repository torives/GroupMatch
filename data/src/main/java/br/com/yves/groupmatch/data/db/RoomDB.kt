package br.com.yves.groupmatch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.calendar.CalendarRoom
import br.com.yves.groupmatch.data.db.calendar.CalendarRoomDAO
import br.com.yves.groupmatch.data.db.calendar.CalendarSourceConverter
import br.com.yves.groupmatch.data.db.timeSlot.CalendarTimeSlotRoom
import br.com.yves.groupmatch.data.db.timeSlot.CalendarTimeSlotRoomDAO

@Database(
		entities = [CalendarTimeSlotRoom::class, CalendarRoom::class],
		version = 3
)
@TypeConverters(LocalDateTimeConverter::class, CalendarSourceConverter::class)
abstract class RoomDB : RoomDatabase() {

	abstract fun timeSlotDAO(): CalendarTimeSlotRoomDAO
	abstract fun calendarDAO(): CalendarRoomDAO

	companion object {
		private var INSTANCE: RoomDB? = null

		fun getInstance(): RoomDB {
			return INSTANCE ?: throw IllegalStateException("Must initialize RoomDB on application start")
		}

		fun init(applicationContext: Context) {
			INSTANCE = Room
					.databaseBuilder(applicationContext, RoomDB::class.java, applicationContext.getString(R.string.database_name))
					.fallbackToDestructiveMigration()
					.build()
		}
	}
}


