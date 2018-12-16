package br.com.yves.groupmatch.presentation.factory.sendCalendar

import br.com.yves.groupmatch.data.sendCalendar.CalendarArchiverFactory
import br.com.yves.groupmatch.domain.sendCalendar.BluetoothSenderService
import br.com.yves.groupmatch.domain.sendCalendar.SendCalendar

object SendCalendarFactory {
    fun create(bluetoothService: BluetoothSenderService) = SendCalendar(bluetoothService, CalendarArchiverFactory.create())
}