package br.com.yves.groupmatch.presentation.ui.bluetooth.client

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import br.com.yves.groupmatch.BuildConfig
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.sendCalendar.CalendarArchiverFactory
import br.com.yves.groupmatch.domain.loadCalendar.LoadCalendar
import br.com.yves.groupmatch.domain.sendCalendar.SendCalendar
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.ui.bluetooth.BluetoothMessageHandler
import br.com.yves.groupmatch.presentation.ui.bluetooth.ClientBluetoothMessageHandler
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.ClientBluetoothService
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.MatchResultViewModel
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.ServerBluetoothService
import com.google.gson.Gson

data class BluetoothServer(val name: String, val address: String)
data class BluetoothClient(val name: String, val status: BluetoothClientStatus) {
	enum class BluetoothClientStatus {
		Connected, TransferringData, DataTransferComplete, DataTransferFailed, Disconnected
	}
}

enum class BluetoothConnectionState {
	Connected, Connecting, Disconnected, Idle
}

class ClientPresenter(
		private val view: BluetoothView,
		private val bluetoothAdapter: BluetoothAdapter,
		private val getCalendar: LoadCalendar
) : BluetoothMessageHandler.Listener {

	private val bluetoothService = ClientBluetoothService(ClientBluetoothMessageHandler(this))
	private val sendCalendar by lazy { SendCalendar(bluetoothService, CalendarArchiverFactory.create()) }

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

	private fun onNewDeviceFound(device: BluetoothDevice) {
		if (device.bondState != BluetoothDevice.BOND_BONDED) {
			val server = BluetoothServer(
					device.name ?: "desconhecido",
					device.address)
			view.displayNewServer(server)
		}
	}

	private fun stopDiscovery() {
		Log.i(TAG, "stopDiscovery()")

		bluetoothAdapter.cancelDiscovery()
		view.toggleProgressBarVisibility(false)
		view.setServerSearchButtonImage(R.drawable.ic_bluetooth_searching)
	}

	//region BluetoothMessageHandler.Listener
	override fun onMessageRead(message: String) {
		if (BuildConfig.DEBUG) {
			view.displayToast(message)
		}
		Log.d(TAG, "Received message: $message")

		try {
			val result = Gson().fromJson(message, MatchResultViewModel::class.java)
			//view.navigateToMatchResult(result)
		} catch (ex: Exception) {
			Log.e(TAG, "Failed to parse MatchResult", ex)
		}
	}

	override fun onDeviceConnected(device: BluetoothDevice) {
		view.showWaitingMatch()

		runOnBackground {
			val calendar = getCalendar.execute()
			sendCalendar.with(calendar).execute()
		}
	}

	override fun onConnectionStateChange(newState: BluetoothConnectionState) {
		view.displayToast(newState.toString())
	}

	override fun onFailToConnect(device: BluetoothDevice) {
		view.displayToast(R.string.failed_to_connect_to_server)
		view.resetState()
	}

	override fun onConnectionLost(device: BluetoothDevice) {
		view.displayToast(R.string.lost_connection_to_server)
		view.resetState()
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
				val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
				onNewDeviceFound(device)
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
				stopDiscovery()
			}
		}
	}

	companion object {
		private val TAG = ClientPresenter::class.java.simpleName
	}
}