package br.com.yves.groupmatch.presentation.ui.bluetooth

import br.com.yves.groupmatch.domain.checkBluetoothAvailability.BluetoohStatus
import br.com.yves.groupmatch.domain.checkBluetoothAvailability.CheckBluetoothAvailability

class BluetoothAvailabilityPresenter(
	private val view: BluetoothAvailabilityView,
	private val checkBluetoothAvailability: CheckBluetoothAvailability
) {

	fun onViewCreated() {
		checkBluetoothAvailability()
	}

	private fun checkBluetoothAvailability() {
		when (checkBluetoothAvailability.execute()) {
			BluetoohStatus.Available -> {
			}
			BluetoohStatus.TurnedOff -> {
				view.displayErrorDialog(
					"Ops!",
					"Você precisa habilitar o Bluetooth para continuar. Deseja fazer isto agora?",
					positiveCallback = { view.navigateToBluetoothOptions() },
					negativeCallback = { view.navigateToCalendarView() }
				)
			}
			BluetoohStatus.NoBLESupport -> {
				displayNoFeatureAvailableErrorDialog()
			}
			BluetoohStatus.NoAdvertisementSupport -> {
				displayNoFeatureAvailableErrorDialog()
			}
		}
	}

	private fun displayNoFeatureAvailableErrorDialog() {
		view.displayErrorDialog(
			"Ops!",
			"Infelizmente o seu aparelho não suporta esta funcionalidade 😔",
			positiveCallback = { view.navigateToCalendarView() }
		)
	}

	fun onServerButtonClicked() {
		view.navigateToBluetoothServerView()
	}

	fun onClientButtonClicked() {
		view.navigateToBluetoothClientView()
	}

	fun onBluetoothEnabled() {
		checkBluetoothAvailability()
	}
}