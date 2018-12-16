package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.showCalendar.Calendar

class SendCalendar(
        private val bluetoothService: BluetoothSenderService,
        private val encoder: CalendarArchiver
) : UseCase<Unit>() {
    private lateinit var calendar: Calendar

    fun with(calendar: Calendar): SendCalendar {
        this.calendar = calendar
        return this
    }

    override fun execute() {
        val transferCalendar = TransferCalendarFactory.create(calendar)
        val payload = encoder.encode(transferCalendar)
        bluetoothService.send(payload)
    }
}