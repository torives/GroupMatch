package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.os.Message
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.ServerBluetoothService
import java.lang.ref.WeakReference

/**
 * The Handler that gets information back from the ServerBluetoothService
 */
class BluetoothMessageHandler(listener: Listener) : Handler() {

	interface Listener {
		fun onMessageRead(message: String)
		fun onDeviceConnected(deviceName: String)
		fun onConnectionStateChange(newState: BluetoothConnectionState)
		fun onFailToConnect(device: BluetoothDevice)
		fun onConnectionLost(device: BluetoothDevice)
	}

	private val reference = WeakReference<Listener>(listener)
	private val listener: Listener?
		get() = reference.get()

	override fun handleMessage(msg: Message) {
		when (msg.what) {
			MESSAGE_STATE_CHANGE -> when (msg.arg1) {
				ServerBluetoothService.STATE_CONNECTED -> {
					listener?.onConnectionStateChange(BluetoothConnectionState.Connected)
				}
				ServerBluetoothService.STATE_CONNECTING -> {
					listener?.onConnectionStateChange(BluetoothConnectionState.Connecting)
				}
				ServerBluetoothService.STATE_LISTEN -> {
					listener?.onConnectionStateChange(BluetoothConnectionState.Disconnected)
				}
				ServerBluetoothService.STATE_NONE -> {
					listener?.onConnectionStateChange(BluetoothConnectionState.Idle)
				}
			}
			MESSAGE_WRITE -> { TODO("Criar o fluxo de exibição do resultado do match") }
			MESSAGE_READ -> {
				val readBuf = msg.obj as ByteArray
				val readMessage = String(readBuf, 0, msg.arg1)

				listener?.onMessageRead(readMessage)
			}
			MESSAGE_CONNECTION_FAILED -> {
				val device = msg.obj as BluetoothDevice
				listener?.onFailToConnect(device)
			}
			MESSAGE_CONNECTION_LOST -> {
				val device = msg.obj as BluetoothDevice
				listener?.onConnectionLost(device)
			}
		}
	}

	companion object {
		const val MESSAGE_STATE_CHANGE = 1
		const val MESSAGE_READ = 2
		const val MESSAGE_WRITE = 3
		const val MESSAGE_CONNECTION_FAILED = 4
		const val MESSAGE_CONNECTION_LOST = 5
	}
}