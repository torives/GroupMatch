package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.os.Handler
import android.os.Message
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.Constants
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
	}

	private val reference = WeakReference<Listener>(listener)
	private val listener: Listener?
		get() = reference.get()

	override fun handleMessage(msg: Message) {
		when (msg.what) {
			Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
				ServerBluetoothService.STATE_CONNECTED -> {
					listener?.onConnectionStateChange(BluetoothConnectionState.Connected)
				}
				ServerBluetoothService.STATE_CONNECTING -> {
					listener?.onConnectionStateChange(BluetoothConnectionState.Connecting)
				}
				ServerBluetoothService.STATE_LISTEN -> {
					listener?.onConnectionStateChange(BluetoothConnectionState.Idle)
				}
				ServerBluetoothService.STATE_NONE -> {
					listener?.onConnectionStateChange(BluetoothConnectionState.None)
				}
			}
			Constants.MESSAGE_WRITE -> {
			}
			Constants.MESSAGE_READ -> {
				val readBuf = msg.obj as ByteArray
				val readMessage = String(readBuf, 0, msg.arg1)

				listener?.onMessageRead(readMessage)
			}
			//Acabou de se conectar
			Constants.MESSAGE_DEVICE_NAME -> {
				// save the connected device's name
				msg.data.getString(Constants.DEVICE_NAME)?.let { name ->
					listener?.onDeviceConnected(name)
				}
			}
			Constants.MESSAGE_TOAST -> {
			}
		}
	}
}