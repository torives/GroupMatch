package br.com.yves.groupmatch.presentation.ui.bluetooth.server

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.util.Log
import br.com.yves.groupmatch.BuildConfig
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.domain.compareCalendars.CompareCalendarsFactory
import br.com.yves.groupmatch.domain.createCalendar.CreateCalendarFactory
import br.com.yves.groupmatch.domain.sendCalendar.BusyCalendar
import br.com.yves.groupmatch.presentation.factory.DateRepositoryFactory
import br.com.yves.groupmatch.presentation.ui.bluetooth.BluetoothMessageHandler
import br.com.yves.groupmatch.presentation.ui.bluetooth.ServerBluetoothMessageHandler
import br.com.yves.groupmatch.presentation.ui.bluetooth.client.BluetoothClient
import br.com.yves.groupmatch.presentation.ui.bluetooth.client.BluetoothConnectionState
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.lang.IllegalStateException

class ServerPresenter(
		private val view: ServerView,
		private val bluetoothAdapter: BluetoothAdapter
) : BluetoothMessageHandler.Listener, CoroutineScope {
	override val coroutineContext = Dispatchers.Default
	private val bluetoothService = ServerBluetoothService(ServerBluetoothMessageHandler(this))
	private val receivedCalendars by lazy { mutableListOf<BusyCalendar>() }

	fun onStart() {
		bluetoothService.start()
	}

	fun onDestroy() {
		bluetoothService.stop()
	}

	fun onDiscoverabilityButtonPressed() {
		if (bluetoothAdapter.scanMode != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
				putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABILITY_DURATION)
			}
			view.toggleDiscoverabilityButton(false)
			view.sendIntent(discoverableIntent, REQUEST_ENABLE_DISCOVERABILITY)
		}
	}

	fun onMatchButtonPressed() {
		check(receivedCalendars.isEmpty().not()) {
			"Attempt to start a match with empty client calendar list"
		}
		bluetoothService.pause()

		// calcula o resultado
		val compare = CompareCalendarsFactory.create(
				receivedCalendars,
				CreateCalendarFactory.create(DateRepositoryFactory.create())
		)
		val result = compare.execute()
		Log.i(TAG, result.toString())


		val payload = Gson().toJson(result)
		bluetoothService.write(payload.toByteArray())

		//manda o resultado pra geral
		// cancela as conexao tudo
		// exibe o resultado
	}

	fun onEmptyList() {
		view.toggleMatchButtonVisibility(false)
	}

	fun onActivityResult(requestCode: Int, resultCode: Int) {
		if (requestCode == REQUEST_ENABLE_DISCOVERABILITY) {
			when (resultCode) {
				Activity.RESULT_CANCELED -> {
					view.toggleDiscoverabilityButton(true)
				}
				else -> {
					view.toggleProgressBarVisibility(true)
					launch {
						scheduleInterfaceUpdate()
					}
				}
			}
		}
	}

	private suspend fun scheduleInterfaceUpdate() {
		withContext(Dispatchers.Main) {
			delay(DISCOVERABILITY_DURATION * 1000L)
			view.toggleDiscoverabilityButton(true)
			view.toggleProgressBarVisibility(false)
		}
	}

	//region BluetoothMessageHandler.Listener
	//TODO: identificar de que client é o calendar em questão para atualizar a lista
	override fun onMessageRead(message: String) {
		if(BuildConfig.DEBUG) {
			view.displayToast(message)
			Log.d(TAG, message)
		}
		try {
			val busyCalendar = Gson().fromJson(message, BusyCalendar::class.java)

			if (receivedCalendars.isEmpty()) {
				view.toggleMatchButtonVisibility(true)
			}
			receivedCalendars.add(busyCalendar)
		} catch (ex: Exception) {
			Log.e(TAG, "Failed to parse received message to BusyCalendar", ex)
		}
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

	companion object {
		private val TAG = ServerPresenter::class.java.simpleName
		private const val DISCOVERABILITY_DURATION = 12 //seconds
		private const val REQUEST_ENABLE_DISCOVERABILITY = 100
	}
}