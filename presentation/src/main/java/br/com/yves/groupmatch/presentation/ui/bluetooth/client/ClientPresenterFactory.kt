package br.com.yves.groupmatch.presentation.ui.bluetooth.client

import android.bluetooth.BluetoothAdapter
import br.com.yves.groupmatch.presentation.factory.CalendarRepositoryFactory
import br.com.yves.groupmatch.presentation.factory.showCalendar.LoadCalendarFactory

object ClientPresenterFactory {
	fun createClientPresenter(view: BluetoothView) =
			ClientPresenter(
					view,
					BluetoothAdapter.getDefaultAdapter(),
					LoadCalendarFactory.create(
							CalendarRepositoryFactory.create()
					)
			)
}