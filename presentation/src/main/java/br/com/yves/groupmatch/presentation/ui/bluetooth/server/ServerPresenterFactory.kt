package br.com.yves.groupmatch.presentation.ui.bluetooth.server

import android.bluetooth.BluetoothAdapter
import br.com.yves.groupmatch.presentation.factory.TimeSlotRepositoryFactory
import br.com.yves.groupmatch.presentation.factory.showCalendar.LoadCalendarFactory

object ServerPresenterFactory {
	fun create(view: ServerView) = ServerPresenter(
			view,
			BluetoothAdapter.getDefaultAdapter(),
			LoadCalendarFactory.create(TimeSlotRepositoryFactory.create())
	)
}