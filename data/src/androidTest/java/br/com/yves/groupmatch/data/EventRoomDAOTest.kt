package br.com.yves.groupmatch.data

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.data.db.event.EventRoom
import com.jakewharton.threetenabp.AndroidThreeTen
import com.nhaarman.mockitokotlin2.doReturn
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.nhaarman.mockitokotlin2.mock
import org.threeten.bp.LocalDateTime

@RunWith(AndroidJUnit4::class)
open class EventRoomDAOTest {
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
        val eventRoomDao = roomDB.eventDAO()
        val event = EventRoom(0, LocalDateTime.now())
        //endregion

        //region When
        eventRoomDao.insertOrReplace(event)
        //endregion

        //region Then
        val cursor = roomDB.query("SELECT * FROM ${EventRoom.TABLE_NAME}", null)
        assert(cursor.count == 1)
        //endregion
    }
}