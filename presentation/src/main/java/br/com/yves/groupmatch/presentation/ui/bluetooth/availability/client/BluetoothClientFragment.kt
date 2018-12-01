package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client


import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.ClientBluetoothService
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.Constants
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.ServerBluetoothService
import kotlinx.android.synthetic.main.fragment_bluetooth_client.*
import java.lang.ref.WeakReference

class BluetoothClientFragment : Fragment() {

	private lateinit var bluetoothAdapter: BluetoothAdapter
	private lateinit var serverAdapter: ServerListAdapter
	private lateinit var mBluetoothService: ClientBluetoothService
	private lateinit var mOutStringBuffer: StringBuffer
	private lateinit var mHandler: MessageHandler

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
		serverAdapter = ServerListAdapter { name ->
			// Cancel discovery because it's costly and we're about to connect
			bluetoothAdapter.cancelDiscovery()

			// Get the device MAC address, which is the last 17 chars in the View
			val address = name.substring(name.length - 17)
			connectDevice(address)
		}
		foundServersList.adapter = serverAdapter
		foundServersList.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)

		fab.apply {
			setOnClickListener {
				toggleDiscovery()
			}
		}

		// Register for broadcasts when a device is discovered
		var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
		activity?.registerReceiver(mReceiver, filter)

		// Register for broadcasts when discovery has finished
		filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
		activity?.registerReceiver(mReceiver, filter)
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		mHandler = MessageHandler(activity!!)
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

		bluetoothAdapter.cancelDiscovery()
		activity?.unregisterReceiver(mReceiver)
		mBluetoothService.stop()
	}

	private fun setupChat() {
		super.onStart()

		// Get the local Bluetooth adapter
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
		// Initialize the ServerBluetoothService to perform bluetooth connections
		mBluetoothService = ClientBluetoothService(activity, mHandler)
		// Initialize the buffer for outgoing messages
		mOutStringBuffer = StringBuffer("")
	}


	private fun toggleDiscovery() {
		// If we're already discovering, stop it
		if (bluetoothAdapter.isDiscovering) {
			stopDiscovery()
		} else {
			startDiscovery()
		}
	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private fun startDiscovery() {
		Log.d(TAG, "startDiscovery()")

		// Indicate scanning in the title
		activity?.actionBar?.setSubtitle(R.string.scanning)
		fab.setImageResource(R.drawable.ic_close)
		serverAdapter.clear()
		progressIndicator.visibility = VISIBLE

		//TODO: MOSTRAR OU NÃO OS DEVICES JÁ PAREADOS?

		// Get a set of currently paired devices
		val pairedDevices = bluetoothAdapter.bondedDevices

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size > 0) {
			for (device in pairedDevices) {
				serverAdapter.add(device.name + "\n" + device.address)
			}
		}
		// Request discover from BluetoothAdapter
		bluetoothAdapter.startDiscovery()
	}

	private fun stopDiscovery() {
		Log.d(TAG, "stopDiscovery()")

		bluetoothAdapter.cancelDiscovery()
		progressIndicator.visibility = GONE
		activity?.actionBar?.setSubtitle(R.string.select_device)
		fab.setImageResource(R.drawable.ic_bluetooth_searching)
	}

	/**
	 * Establish connection with other device
	 *
	 * @param data   An [Intent] with [DeviceListActivity.EXTRA_DEVICE_ADDRESS] extra.
	 * @param secure Socket Security type - Secure (true) , Insecure (false)
	 */
	private fun connectDevice(address: String, secure: Boolean = true) {
		// Get the BluetoothDevice object
		val device = bluetoothAdapter.getRemoteDevice(address)
		// Attempt to connect to the device
		mBluetoothService.connect(device, secure)
	}

	/**
	 * The BroadcastReceiver that listens for discovered devices and changes the title when
	 * discovery is finished
	 */
	private val mReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			val action = intent.action

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND == action) {
				// Get the BluetoothDevice object from the Intent
				val device =
						intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
				// If it's already paired, skip it, because it's been listed already
				if (device.bondState != BluetoothDevice.BOND_BONDED) {
					serverAdapter.add(device.name + "\n" + device.address)
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
				stopDiscovery()
				if (serverAdapter.itemCount == 0) {
					val noDevices = resources.getText(R.string.none_found).toString()
					serverAdapter.add(noDevices)
				}
			}
		}
	}

	companion object {
		private val TAG = BluetoothClientFragment::class.java.simpleName
		private const val SCAN_PERIOD = 5000L
	}
}

/**
 * The Handler that gets information back from the ServerBluetoothService
 */
private class MessageHandler(activity: Activity) : Handler() {
	private val reference = WeakReference<Activity>(activity)
	private val activity: Activity?
		get() = reference.get()

	override fun handleMessage(msg: Message) {
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
			//Acabou de se conectar
			Constants.MESSAGE_DEVICE_NAME -> {
				// save the connected device's name
				val mConnectedDeviceName = msg.data.getString(Constants.DEVICE_NAME)
				activity?.let {
					Toast.makeText(
							activity,
							"Connected to $mConnectedDeviceName",
							Toast.LENGTH_SHORT
					).show()
				}
			}
			Constants.MESSAGE_TOAST -> activity?.let {
				Toast.makeText(
						activity, msg.data.getString(Constants.TOAST),
						Toast.LENGTH_SHORT
				).show()
			}
		}
	}
}
