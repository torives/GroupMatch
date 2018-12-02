package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.domain.showCalendar.ShowCalendar
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.ClientBluetoothService
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.ServerBluetoothService

data class BluetoothServer(val name: String, val address: String)
data class BluetoothClient(val name: String)

enum class BluetoothConnectionState {
	Connected, Connecting, Disconnected, Idle, None
}

class ClientPresenter(
		private val view: BluetoothView,
		private var bluetoothAdapter: BluetoothAdapter,
		private var getCalendar: ShowCalendar
) : BluetoothMessageHandler.Listener {

	private val bluetoothService = ClientBluetoothService(BluetoothMessageHandler(this))

	fun onViewCreated() {
		var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
		view.registerBroadcastReceiver(bluetoothBroadcastReceiver, filter)

		filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
		view.registerBroadcastReceiver(bluetoothBroadcastReceiver, filter)
	}

	fun onResume() {
		// Only if the state is STATE_NONE, do we know that we haven't started already
		if (bluetoothService.state == ServerBluetoothService.STATE_NONE) {
			bluetoothService.start()
		}
	}

	fun onDestroy() {
		bluetoothAdapter.cancelDiscovery()
		bluetoothService.stop()
		view.unregisterBroadcastReceiver(bluetoothBroadcastReceiver)
	}

	fun onClickServerSearchButton() {
		if (bluetoothAdapter.isDiscovering) {
			stopDiscovery()
		} else {
			startDiscovery()
		}
	}

	fun onServerSelected(server: BluetoothServer) {
		bluetoothAdapter.cancelDiscovery()

		view.showWaitingConnection()

		val address = server.address
		connectDevice(address)
	}

	private fun connectDevice(address: String, secure: Boolean = true) {
		val device = bluetoothAdapter.getRemoteDevice(address)
		bluetoothService.connect(device, secure)
	}

	private fun startDiscovery() {
		Log.i(TAG, "startDiscovery()")

		view.setTitle(R.string.scanning)
		view.setServerSearchButtonImage(R.drawable.ic_close)
		view.clearServerList()
		view.toggleProgressBarVisibility(true)

		//TODO: MOSTRAR OU NÃO OS DEVICES JÁ PAREADOS?
		displayPairedDevices()

		bluetoothAdapter.startDiscovery()
	}

	private fun displayPairedDevices() {
		val pairedDevices = bluetoothAdapter.bondedDevices

		pairedDevices.map {
			BluetoothServer(it.name, it.address)
		}.also {
			view.displayNewServers(it)
		}
	}

	private fun stopDiscovery() {
		Log.i(TAG, "stopDiscovery()")

		bluetoothAdapter.cancelDiscovery()
		view.toggleProgressBarVisibility(false)
		view.setTitle(R.string.select_device)
		view.setServerSearchButtonImage(R.drawable.ic_bluetooth_searching)
	}

	//region BluetoothMessageHandler.Listener
	override fun onMessageRead(message: String) {
		view.displayToast(message)
	}

	override fun onDeviceConnected(deviceName: String) {
		view.displayToast(deviceName)
	}

	override fun onConnectionStateChange(newState: BluetoothConnectionState) {
		view.displayToast(newState.toString())
		when(newState) {
			BluetoothConnectionState.Connected -> view.showWaitingMatch()
		}
	}
	//endregion

	/**
	 * The BroadcastReceiver that listens for discovered devices and changes the title when
	 * discovery is finished
	 */
	private val bluetoothBroadcastReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			val action = intent.action

			if (BluetoothDevice.ACTION_FOUND == action) {
				val device =
						intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
				if (device.bondState != BluetoothDevice.BOND_BONDED) {
					val server = BluetoothServer(device.name
							?: "desconhecido", device.address)
					view.displayNewServer(server)
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
				stopDiscovery()
			}
		}
	}

	companion object {
		private val TAG = ClientPresenter::class.java.simpleName
	}
}