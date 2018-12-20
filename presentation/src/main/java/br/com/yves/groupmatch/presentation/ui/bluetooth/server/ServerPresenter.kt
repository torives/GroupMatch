package br.com.yves.groupmatch.presentation.ui.bluetooth.server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.util.Log
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.domain.compareCalendars.CompareCalendars
import br.com.yves.groupmatch.domain.compareCalendars.CompareCalendarsFactory
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendarFactory
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendar
import br.com.yves.groupmatch.presentation.factory.DateRepositoryFactory
import br.com.yves.groupmatch.presentation.ui.bluetooth.BluetoothMessageHandler
import br.com.yves.groupmatch.presentation.ui.bluetooth.ServerBluetoothMessageHandler
import br.com.yves.groupmatch.presentation.ui.bluetooth.client.BluetoothClient
import br.com.yves.groupmatch.presentation.ui.bluetooth.client.BluetoothConnectionState
import com.google.gson.Gson

class ServerPresenter(
		private val view: ServerView,
		private val bluetoothAdapter: BluetoothAdapter
) : BluetoothMessageHandler.Listener {
	private val bluetoothService = ServerBluetoothService(ServerBluetoothMessageHandler(this))
	private val receivedCalendars by lazy { mutableListOf<BusyCalendar>() }

	fun onStart() {
		bluetoothService.start()
	}

	fun onResume() {
		//TODO: stop progress view after the 12seconds of bluetooth visibility
		ensureDiscoverable()
	}

	fun onDestroy() {
		bluetoothService.stop()
	}

	fun onMatchButtonPressed() {
		//TODO:
		// nao deixa mais ngm se conectar
		bluetoothService.pause()

		// calcula o resultado

		val compare = CompareCalendarsFactory.create(receivedCalendars, CreateCalendarFactory.create(DateRepositoryFactory.create()))
		val result = compare.execute()
		Log.i(TAG, result.toString())
		//manda o resultado pra geral
		// cancela as conexao tudo
		// exibe o resultado
	}

	fun onEmptyList() {
		view.toggleMatchButtonVisibility(false)
	}

	//region BluetoothMessageHandler.Listener
	//TODO: identificar de que client é o calendar em questão para atualizar a lista
	override fun onMessageRead(message: String) {
		view.displayToast(message)
		try {
			val busyCalendar = Gson().fromJson(message, BusyCalendar::class.java)

			if (receivedCalendars.isEmpty()) {
				view.toggleMatchButtonVisibility(true)
			}
			receivedCalendars.add(busyCalendar)
		} catch (exception: Exception) {
			Log.e(TAG, "Failed to parse received message to BusyCalendar")
		}
		Log.d(TAG, message)
	}

	override fun onDeviceConnected(device: BluetoothDevice) {
		view.displayNewClient(
				BluetoothClient(
						device.name,
						BluetoothClient.BluetoothClientStatus.Connected
				)
		)
	}

	override fun onConnectionStateChange(newState: BluetoothConnectionState) {
		view.displayToast(newState.toString())
		when (newState) {
			BluetoothConnectionState.Idle -> {
			}
			BluetoothConnectionState.Connecting -> {
			}
			BluetoothConnectionState.Connected -> {
			}
			BluetoothConnectionState.Disconnected -> {
			}
		}
	}

	override fun onFailToConnect(device: BluetoothDevice) {
		view.displayToast(R.string.failed_to_connect_to_client)
	}

	override fun onConnectionLost(device: BluetoothDevice) {
		view.displayToast(R.string.lost_connection_to_client)
		view.removeClient(
				BluetoothClient(
						device.name,
						BluetoothClient.BluetoothClientStatus.Disconnected
				)
		)
	}
	//endregion

	/**
	 * Makes this device discoverable.
	 */
	private fun ensureDiscoverable() {
		if (bluetoothAdapter.scanMode != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 12)
			view.sendIntent(discoverableIntent)
		}
	}

	companion object {
		private val TAG = ServerPresenter::class.java.simpleName
	}
}