package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client


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
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.BluetoothChatService
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.Constants
import kotlinx.android.synthetic.main.fragment_bluetooth_client.*

class BluetoothClientFragment : Fragment() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var serverAdapter: ServerListAdapter
    private lateinit var mChatService: BluetoothChatService
    private lateinit var mOutStringBuffer: StringBuffer

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
    }

    override fun onStart() {
        super.onStart()
        setupChat()
    }

    override fun onResume() {
        super.onResume()
        // Only if the state is STATE_NONE, do we know that we haven't started already
        if (mChatService.state == BluetoothChatService.STATE_NONE) {
            // Start the Bluetooth chat services
            mChatService.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        bluetoothAdapter.cancelDiscovery()
        activity?.unregisterReceiver(mReceiver)
        mChatService.stop()
    }

    private fun setupChat() {
        super.onStart()

        // Get the local Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = BluetoothChatService(activity, mHandler)
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = StringBuffer("")
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
     * Establish connection with other divice
     *
     * @param data   An [Intent] with [DeviceListActivity.EXTRA_DEVICE_ADDRESS] extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private fun connectDevice(address: String, secure: Boolean = true) {
        // Get the BluetoothDevice object
        val device = bluetoothAdapter.getRemoteDevice(address)
        // Attempt to connect to the device
        mChatService.connect(device, secure)
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

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val activity = activity
            when (msg.what) {
                Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                    BluetoothChatService.STATE_CONNECTED -> {
//						setStatus(getString(R.string.title_connected_to, mConnectedDeviceName))
//						mConversationArrayAdapter.clear()
                    }
                    BluetoothChatService.STATE_CONNECTING -> {
                    }//setStatus(R.string.title_connecting)
                    BluetoothChatService.STATE_LISTEN, BluetoothChatService.STATE_NONE -> {
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
                    //mConnectedDeviceName = msg.data.getString(Constants.DEVICE_NAME)
                    activity?.let {
                        Toast.makeText(
                                activity,
                                "nome do device aqui",//"Connected to $mConnectedDeviceName",
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

    companion object {
        private val TAG = BluetoothClientFragment::class.java.simpleName
        private const val SCAN_PERIOD = 5000L
    }
}
