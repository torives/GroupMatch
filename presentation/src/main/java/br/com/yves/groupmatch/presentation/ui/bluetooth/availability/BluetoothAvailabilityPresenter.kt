package br.com.yves.groupmatch.presentation.ui.bluetooth.availability

import br.com.yves.groupmatch.domain.checkBluetoothAvailability.BluetoothStatus
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
			BluetoothStatus.Available -> {
			}
			BluetoothStatus.Disabled -> {
				view.displayErrorDialog(
					"Ops!",
					"Você precisa habilitar o Bluetooth para continuar. Deseja fazer isto agora?",
					positiveCallback = { view.displayBluetoothActivationDialog() },
					negativeCallback = { view.navigateToCalendarView() }
				)
			}
			BluetoothStatus.Unsupported -> {
				displayNoFeatureAvailableErrorDialog()
			}
			BluetoothStatus.NoLocationPermission -> {
				view.displayLocationPermissionDialog()
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

	fun onLocationPermissionGiven() {
		checkBluetoothAvailability()
	}
}