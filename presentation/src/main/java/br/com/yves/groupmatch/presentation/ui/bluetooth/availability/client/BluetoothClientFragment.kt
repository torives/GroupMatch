package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.fragment_bluetooth_client.*

class BluetoothClientFragment : Fragment() {

	private lateinit var bluetoothAdapter: BluetoothAdapter
	private lateinit var serverAdapter: ServerListAdapter

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

		fab.apply {
			setOnClickListener {
				doDiscovery()
				hide()
			}
		}

		// Register for broadcasts when a device is discovered
		var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
		activity?.registerReceiver(mReceiver, filter)

		// Register for broadcasts when discovery has finished
		filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
		activity?.registerReceiver(mReceiver, filter)

		// Get the local Bluetooth adapter
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

		// Get a set of currently paired devices
		val pairedDevices = bluetoothAdapter.getBondedDevices()

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size > 0) {
			for (device in pairedDevices) {
				serverAdapter.add(device.name + "\n" + device.address)
			}
		}
//		val serverIntent = Intent(activity, DeviceListActivity::class.java)
//		startActivityForResult(serverIntent, 1)
	}

	override fun onDestroy() {
		super.onDestroy()

		bluetoothAdapter.cancelDiscovery()
		activity?.unregisterReceiver(mReceiver)
	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private fun doDiscovery() {
		Log.d(TAG, "doDiscovery()")

		// Indicate scanning in the title
		activity?.setTitle(R.string.scanning)

		// If we're already discovering, stop it
		if (bluetoothAdapter.isDiscovering) {
			bluetoothAdapter.cancelDiscovery()
		}

		// Request discover from BluetoothAdapter
		bluetoothAdapter.startDiscovery()
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
				activity?.setTitle(R.string.select_device)
				fab.show()
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
