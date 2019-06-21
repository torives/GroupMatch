package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.models.calendar.Calendar
import br.com.yves.groupmatch.domain.UseCase

class SendCalendar(
		private val bluetoothService: BluetoothService,
		private val encoder: CalendarEncoder
) : UseCase<Unit>() {
	private lateinit var calendar: Calendar

	fun with(calendar: Calendar): SendCalendar {
		this.calendar = calendar
		return this
	}

	override fun execute() {
		val payload = encoder.encode(calendar)
		bluetoothService.send(payload as ByteArray)
	}
}