package br.com.yves.groupmatch.presentation

import android.app.Application
import br.com.yves.groupmatch.data.db.RoomDB
import com.jakewharton.threetenabp.AndroidThreeTen

class Application : Application() {

	override fun onCreate() {
		super.onCreate()

		INSTANCE = this

		AndroidThreeTen.init(this)
		RoomDB.init(this)
	}

	companion object {
		lateinit var INSTANCE: Application
	}
}