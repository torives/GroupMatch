package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server


import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.ServerListAdapter
import kotlinx.android.synthetic.main.fragment_bluetooth_client.*
import java.util.*


const val SERVICE_STRING = "7D2EA28A-F7BD-485A-BD9D-92AD6ECFE93E"
val SERVICE_UUID: UUID = UUID.fromString(SERVICE_STRING)

class BluetoothServerFragment : Fragment() {

	private lateinit var bluetoothAdapter: BluetoothAdapter
	private lateinit var serverAdapter: ServerListAdapter
	private lateinit var mBluetoothService: ServerBluetoothService
	private lateinit var mOutStringBuffer: StringBuffer

	//region Lifecycle
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_bluetooth_client, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		foundServersList.layoutManager = LinearLayoutManager(context)
		serverAdapter = ServerListAdapter()
		foundServersList.adapter = serverAdapter
		foundServersList.addItemDecoration(
			DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)

		fab.setOnClickListener {
			ensureDiscoverable()
		}
	}

	override fun onStart() {
		super.onStart()
		setupChat()
	}

	override fun onResume() {
		super.onResume()
		// Only if the state is STATE_NONE, do we know that we haven't started already
		if (mBluetoothService.state == ServerBluetoothService.STATE_NONE) {
			// Start the Bluetooth chat services
			mBluetoothService.start()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		mBluetoothService.stop()
	}
	//endregion

	private fun setupChat() {
		super.onStart()

		// Get the local Bluetooth adapter
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
		// Initialize the ServerBluetoothService to perform bluetooth connections
		mBluetoothService = ServerBluetoothService(activity, mHandler)
		// Initialize the buffer for outgoing messages
		mOutStringBuffer = StringBuffer("")
	}

	/**
	 * Makes this device discoverable.
	 */
	private fun ensureDiscoverable() {
		if (bluetoothAdapter.scanMode != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
			startActivity(discoverableIntent)
		}
	}


	/**
	 * The Handler that gets information back from the ServerBluetoothService
	 */
	private val mHandler = object : Handler() {
		override fun handleMessage(msg: Message) {
			val activity = activity
			when (msg.what) {
				Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
					ServerBluetoothService.STATE_CONNECTED -> {
//						setStatus(getString(R.string.title_connected_to, mConnectedDeviceName))
//						mConversationArrayAdapter.clear()
					}
					ServerBluetoothService.STATE_CONNECTING -> {
					}//setStatus(R.string.title_connecting)
					ServerBluetoothService.STATE_LISTEN, ServerBluetoothService.STATE_NONE -> {
					}
//						setStatus(
//						R.string.title_not_connected
//					)
				}
				Constants.MESSAGE_WRITE -> {
					val writeBuf = msg.obj as ByteArray
					// construct a string from the buffer
					val writeMessage = String(writeBuf)
					//mConversationArrayAdapter.add("Me:  $writeMessage")
				}
				Constants.MESSAGE_READ -> {
					val readBuf = msg.obj as ByteArray
					// construct a string from the valid bytes in the buffer
					val readMessage = String(readBuf, 0, msg.arg1)
					//mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage)
				}
				Constants.MESSAGE_DEVICE_NAME -> {
					// save the connected device's name
					val connectedDeviceName = msg.data.getString(Constants.DEVICE_NAME)
					if (null != activity) {
						Toast.makeText(
							activity,
								"Connected to $connectedDeviceName",
							Toast.LENGTH_SHORT
						).show()

						serverAdapter.add(connectedDeviceName)
					}
				}
				Constants.MESSAGE_TOAST -> if (null != activity) {
					Toast.makeText(
						activity, msg.data.getString(Constants.TOAST),
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}

	companion object {
		private val TAG = ::BluetoothServerFragment.name
	}
}
