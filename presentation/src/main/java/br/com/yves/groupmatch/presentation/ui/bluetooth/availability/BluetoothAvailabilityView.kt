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

	companion object {
		const val ENABLE_BLUETOOTH_REQUEST = 100
		const val ENABLE_BLUETOOTH_RESPONSE_ALLOW = -1
		const val LOCATION_PERMISSION_REQUEST = 200
	}
}