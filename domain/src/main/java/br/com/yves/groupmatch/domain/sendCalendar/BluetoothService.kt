package br.com.yves.groupmatch.domain.sendCalendar

interface BluetoothService {
    val deviceName: String
    fun send(message: ByteArray)
}