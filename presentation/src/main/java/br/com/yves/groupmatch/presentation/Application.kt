package br.com.yves.groupmatch.presentation

import android.app.Application
import br.com.yves.groupmatch.data.db.RoomDB
import com.jakewharton.threetenabp.AndroidThreeTen

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        RoomDB.init(this)
    }
}