package br.com.yves.groupmatch.presentation

import android.app.Application
import br.com.yves.groupmatch.BuildConfig
import br.com.yves.groupmatch.data.auth.GroupMatchAuth
import br.com.yves.groupmatch.data.db.RoomDB
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.jakewharton.threetenabp.AndroidThreeTen
import io.fabric.sdk.android.Fabric

class GroupMatchApplication : Application() {

	override fun onCreate() {
		super.onCreate()

		instance = this

		AndroidThreeTen.init(this)
		RoomDB.init(this)
		GroupMatchAuth.init(this)

		configureCrashlytics()
	}

	private fun configureCrashlytics() {
		val crashlyticsKit = Crashlytics.Builder()
				.core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
				.build()
		Fabric.with(this, crashlyticsKit)
	}

	companion object {
		lateinit var instance: Application
	}
}