package br.com.yves.groupmatch.data.loadEvents

import android.content.Context
import androidx.room.Room
import br.com.yves.groupmatch.data.db.RoomDB
import br.com.yves.groupmatch.domain.loadEvents.Event
import br.com.yves.groupmatch.domain.loadEvents.EventRepository
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import br.com.yves.groupmatch.data.R
import br.com.yves.groupmatch.data.db.event.EventRoom

class EventRepositoryImpl(context: Context) : EventRepository {
    private val database: RoomDB =
            Room.databaseBuilder(context, RoomDB::class.java, context.getString(R.string.database_name)).build()

    override fun getEventsAt(date: LocalDate): List<Event> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEventsBetween(initialDate: LocalDateTime, finalDate: LocalDateTime): List<Event> {
        val events = database.eventDAO().getAllEventsBetween(initialDate.toString(), finalDate.toString())
        return events.map { EventMapper.createFrom(it) }
    }
}

object EventMapper {
    fun createFrom(event: Event) = EventRoom(0, event.date)
    fun createFrom(event: EventRoom) = Event(event.date)
}