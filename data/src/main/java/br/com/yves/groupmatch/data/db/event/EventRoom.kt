package br.com.yves.groupmatch.data.db.event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.yves.groupmatch.data.db.event.EventRoom.Companion.COLUMN_ID
import br.com.yves.groupmatch.data.db.event.EventRoom.Companion.TABLE_NAME
import org.threeten.bp.LocalDateTime

@Entity(tableName = TABLE_NAME,
        indices = [Index(COLUMN_ID, unique = true)])
open class EventRoom(
        @ColumnInfo(name = COLUMN_ID) @PrimaryKey val id: Int,
        @ColumnInfo(name = COLUMN_DATE) val date: LocalDateTime
) {
    companion object {
        const val TABLE_NAME = "events"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
    }
}