package br.com.yves.groupmatch.presentation.factory.checkBluetoothAvailability

import br.com.yves.groupmatch.data.checkBluetoothAvailability.BluetoothServiceImpl
import br.com.yves.groupmatch.presentation.GroupMatchApplication

object BluetoothServiceFactory {
	fun create() = BluetoothServiceImpl(GroupMatchApplication.instance)
}