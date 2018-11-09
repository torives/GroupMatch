package br.com.yves.groupmatch.presentation.ui.bluetooth

import br.com.yves.groupmatch.domain.checkBluetoothAvailability.BluetoohStatus
import br.com.yves.groupmatch.domain.checkBluetoothAvailability.CheckBluetoothAvailability

interface BluetoothAvailabilityView {
	fun displayErrorDialog(
		title: String,
		message: String,
		callback: ((isAnswerPositive: Boolean) -> Unit)? = null
	)

	fun navigateToBluetoothOptions()
	fun navigateToBluetoothServerView()
	fun navigateToBluetoothClientView()
	fun navigateToCalendarView()
}

class BluetoothAvailabilityPresenter(
	private val view: BluetoothAvailabilityView,
	private val checkBluetoothAvailability: CheckBluetoothAvailability
) {

	fun onViewCreated() {
		when (checkBluetoothAvailability.execute()) {
			BluetoohStatus.Available -> {}
			BluetoohStatus.TurnedOff -> {
				view.displayErrorDialog(
					"Ops!",
					"Você precisa habilitar o Bluetooth para continuar. Deseja fazer isto agora?"
				) { isAnswerPositive ->
					if (isAnswerPositive) {
						view.navigateToBluetoothOptions()
					} else {
						view.navigateToCalendarView()
					}
				}
			}
			BluetoohStatus.NoBLESupport -> {
				view.displayErrorDialog(
					"Ops!",
					"Você precisa habilitar o Bluetooth para continuar. Deseja fazer isto agora?"
				)
			}
			BluetoohStatus.NoAdvertisementSupport -> {
			}
		}
	}

	fun onServerButtonClicked() {
		view.navigateToBluetoothServerView()
	}

	fun onClientButtonClicked() {
		view.navigateToBluetoothClientView()
	}

	fun onBluetoothEnabled() {

	}
}