package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.bluetooth.BluetoothAdapter
import br.com.yves.groupmatch.presentation.factory.TimeSlotRepositoryFactory
import br.com.yves.groupmatch.presentation.factory.showCalendar.ShowCalendarFactory

object ClientPresenterFactory {
    fun createClientPresenter(view: BluetoothView) =
            ClientPresenter(
                    view,
                    BluetoothAdapter.getDefaultAdapter(),
                    ShowCalendarFactory.create(
                            TimeSlotRepositoryFactory.create()
                    )
            )
}