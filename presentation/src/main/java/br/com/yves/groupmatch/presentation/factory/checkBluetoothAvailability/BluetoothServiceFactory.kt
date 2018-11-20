package br.com.yves.groupmatch.presentation.factory.checkBluetoothAvailability

import br.com.yves.groupmatch.data.checkBluetoothAvailability.BluetoothServiceImpl
import br.com.yves.groupmatch.presentation.Application

object BluetoothServiceFactory {
	fun create() = BluetoothServiceImpl(Application.instance)
}