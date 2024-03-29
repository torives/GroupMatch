package br.com.yves.groupmatch.presentation.factory.checkBluetoothAvailability

import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.BluetoothAvailabilityPresenter
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.BluetoothAvailabilityView

object BluetoothAvailabilityPresenterFactory {
	fun create(view: BluetoothAvailabilityView) =
		BluetoothAvailabilityPresenter(
			view,
			CheckBluetoothAvailabilityFactory.create()
		)
}