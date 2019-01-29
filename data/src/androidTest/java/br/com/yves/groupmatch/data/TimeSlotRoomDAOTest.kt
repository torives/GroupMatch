package br.com.yves.groupmatch.data

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.timeSlot.TimeSlotRoom
import com.jakewharton.threetenabp.AndroidThreeTen
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.TemporalAdjusters

@RunWith(AndroidJUnit4::class)
open class TimeSlotRoomDAOTest {
	private lateinit var roomDB: RoomDB

	@Before
	fun setup() {
		val context = InstrumentationRegistry.getContext()
		roomDB = Room.inMemoryDatabaseBuilder(context, RoomDB::class.java).build()
		AndroidThreeTen.init(context)
	}

	@After
	fun tearDown() {
		roomDB.close()
	}

	@Test
	fun insertEventRoom() {
		//region Given
		val eventRoomDao = roomDB.timeSlotDAO()
		val event = TimeSlotRoom(0, LocalDateTime.now())
		//endregion

		//region When
		eventRoomDao.insert(event)
		//endregion

		//region Then
		val cursor = roomDB.query("SELECT * FROM ${TimeSlotRoom.TABLE_NAME}", null)
		assert(cursor.count == 1)
		//endregion
	}

	@Test
	fun loadEventBetweenDates() {
		//region Given
		val eventRoomDao = roomDB.timeSlotDAO()
		val initialDay = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
		val finalDay = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
		val event = TimeSlotRoom(0, LocalDateTime.now())

		eventRoomDao.insert(event)
		//endregion


		//region When
		val response =
			eventRoomDao.getAllTimeSlotsBetween(initialDay.toString(), finalDay.toString())
		//endregion

		//region Then
		assert(response.isNotEmpty())
		//endregion
	}

	@Test
	fun loadEventBetweenDatesAndWithSameDayOfTheFirstDate() {
		//region Given
		val eventRoomDao = roomDB.timeSlotDAO()
		val initialDay = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
		val finalDay = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
		val event = TimeSlotRoom(0, initialDay)

		eventRoomDao.insert(event)
		//endregion


		//region When
		val response =
			eventRoomDao.getAllTimeSlotsBetween(initialDay.toString(), finalDay.toString())
		//endregion

		//region Then
		assert(response.isNotEmpty())
		//endregion
	}

	@Test
	fun loadEventOutsideDates() {
		//region Given
		val eventRoomDao = roomDB.timeSlotDAO()
		val initialDay = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
		val finalDay = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
		val event = TimeSlotRoom(0, initialDay.with(TemporalAdjusters.firstDayOfNextMonth()))

		eventRoomDao.insert(event)
		//endregion


		//region When
		val response =
			eventRoomDao.getAllTimeSlotsBetween(initialDay.toString(), finalDay.toString())
		//endregion

		//region Then
		assert(response.isEmpty())
		//endregion
	}
}