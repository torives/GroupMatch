package br.com.yves.groupmatch.presentation.factory.sendCalendar

import br.com.yves.groupmatch.data.sendCalendar.CalendarArchiverFactory
import br.com.yves.groupmatch.domain.sendCalendar.BluetoothService
import br.com.yves.groupmatch.domain.sendCalendar.SendCalendar

object SendCalendarFactory {
    fun create(bluetoothService: BluetoothService) = SendCalendar(bluetoothService, CalendarArchiverFactory.create())
}