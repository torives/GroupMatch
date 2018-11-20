package br.com.yves.groupmatch.presentation.ui.bluetooth.availability

interface BluetoothAvailabilityView {
	fun displayErrorDialog(
		title: String,
		message: String,
		positiveCallback: (() -> Unit)? = null,
		negativeCallback: (() -> Unit)? = null
	)
	fun displayBluetoothActivationDialog()
	fun displayLocationPermissionDialog()
	fun navigateToBluetoothServerView()
	fun navigateToBluetoothClientView()
	fun navigateToCalendarView()
}