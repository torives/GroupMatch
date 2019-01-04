package br.com.yves.groupmatch.domain.sendCalendar

import br.com.yves.groupmatch.domain.UseCase
import br.com.yves.groupmatch.domain.loadCalendar.Calendar

class SendCalendar(
		private val bluetoothService: BluetoothService,
		private val encoder: CalendarArchiver
) : UseCase<Unit>() {
	private lateinit var calendar: Calendar

	fun with(calendar: Calendar): SendCalendar {
		this.calendar = calendar
		return this
	}

	override fun execute() {
		val calendar = CalendarFactory.create(calendar, bluetoothService.deviceName)
		val payload = encoder.encode(calendar)
		bluetoothService.send(payload)
	}
}