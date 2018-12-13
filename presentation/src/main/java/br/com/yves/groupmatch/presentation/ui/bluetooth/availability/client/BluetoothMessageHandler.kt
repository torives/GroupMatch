package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.os.Message
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.ServerBluetoothService
import java.lang.ref.WeakReference

/**
 * The Handler that gets information back from the ServerBluetoothService
 */
abstract class BluetoothMessageHandler(listener: Listener) : Handler() {

	interface Listener {
		fun onMessageRead(message: String)
		fun onDeviceConnected(device: BluetoothDevice)
		fun onConnectionStateChange(newState: BluetoothConnectionState)
		fun onFailToConnect(device: BluetoothDevice)
		fun onConnectionLost(device: BluetoothDevice)
	}

	protected val reference = WeakReference<Listener>(listener)
	protected val listener: Listener?
		get() = reference.get()


	override fun handleMessage(msg: Message) {
		when (msg.what) {
			MESSAGE_STATE_CHANGE -> onStateChange(msg.arg1)
			MESSAGE_WRITE -> onMessageWrite(msg)
			MESSAGE_READ -> onMessageRead(msg.obj as ByteArray, msg.arg1)
			MESSAGE_CONNECTION_FAILED -> onConnectionFailed(msg.obj as BluetoothDevice)
			MESSAGE_CONNECTION_LOST -> onConnectionLost(msg.obj as BluetoothDevice)
			MESSAGE_DEVICE_CONNECTED -> onDeviceConnected(msg.obj as BluetoothDevice)
		}
	}

	open fun onStateChange(newState: Int) {
		when (newState) {
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
	}

	open fun onMessageWrite(message: Message) {
		throw NotImplementedError("Subclasses must override onMessageWrite method")
	}

	open fun onMessageRead(buffer: ByteArray, totalBytesRead: Int) {
		val readMessage = String(buffer, 0, totalBytesRead)

		listener?.onMessageRead(readMessage)
	}

	open fun onConnectionFailed(device: BluetoothDevice) {
		listener?.onFailToConnect(device)
	}

	open fun onConnectionLost(device: BluetoothDevice) {
		listener?.onConnectionLost(device)
	}

	private fun onDeviceConnected(device: BluetoothDevice) {
		listener?.onDeviceConnected(device)
	}

	companion object {
		const val MESSAGE_STATE_CHANGE = 1
		const val MESSAGE_READ = 2
		const val MESSAGE_WRITE = 3
		const val MESSAGE_CONNECTION_FAILED = 4
		const val MESSAGE_CONNECTION_LOST = 5
		const val MESSAGE_DEVICE_CONNECTED = 6
	}
}

class ClientBluetoothMessageHandler(listener: BluetoothMessageHandler.Listener) : BluetoothMessageHandler(listener) {

	override fun onMessageWrite(message: Message) {
		TODO("Criar o fluxo de exibição do resultado do match")
	}
}

class ServerBluetoothMessageHandler(listener: BluetoothMessageHandler.Listener) : BluetoothMessageHandler(listener) {

	override fun onMessageWrite(message: Message) {
		TODO("Criar o fluxo de exibição do resultado do match")
	}
}