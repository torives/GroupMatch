package br.com.yves.groupmatch.presentation.factory.checkBluetoothAvailability

import br.com.yves.groupmatch.domain.checkBluetoothAvailability.CheckBluetoothAvailability

object CheckBluetoothAvailabilityFactory {
	fun create() = CheckBluetoothAvailability(BluetoothServiceFactory.create())
}