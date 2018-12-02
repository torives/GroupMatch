package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server


import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.BluetoothClient
import kotlinx.android.synthetic.main.fragment_bluetooth_client.*
import java.util.*


const val SERVICE_STRING = "7D2EA28A-F7BD-485A-BD9D-92AD6ECFE93E"
val SERVICE_UUID: UUID = UUID.fromString(SERVICE_STRING)

class ClientListAdapter(val onItemClick: ((BluetoothClient) -> Unit)? = null) :
		RecyclerView.Adapter<ClientListAdapter.ViewHolder>() {

	private val clients = linkedSetOf<BluetoothClient>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(
				R.layout.item_server_list, //FIXME: criar layout próprio
				parent,
				false
		)
		return ViewHolder(view)
	}

	override fun getItemCount() = clients.count()

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		clients.elementAtOrNull(position)?.let {
			holder.bind(it)
		}
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private val clientName: TextView by lazy {
			itemView.findViewById<TextView>(
					R.id.serverName
			)
		}

		fun bind(client: BluetoothClient) {
			clientName.text = client.name
			clientName.setOnClickListener { onItemClick?.invoke(client) }
		}
	}

	fun add(server: BluetoothClient){
		clients.add(server)
		notifyItemChanged(clients.indices.last)
	}

	fun addAll(servers: Collection<BluetoothClient>){
		val lastPosition = this.clients.indices.last

		this.clients.addAll(servers)
		notifyItemRangeChanged(lastPosition, servers.size)
	}

	fun clear() {
		clients.clear()
		notifyDataSetChanged()
	}
}
class BluetoothServerFragment : Fragment() {

	private lateinit var bluetoothAdapter: BluetoothAdapter
	private lateinit var clientListAdapter: ClientListAdapter
	private lateinit var mBluetoothService: ServerBluetoothService
	private lateinit var mOutStringBuffer: StringBuffer

	//region Lifecycle
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_bluetooth_server, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		foundServersList.layoutManager = LinearLayoutManager(context)
		clientListAdapter = ClientListAdapter()
		foundServersList.adapter = clientListAdapter
		foundServersList.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)

		send.setOnClickListener {
			mBluetoothService.write("Server Mutreta Master".toByteArray())
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

		ensureDiscoverable()
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
					ServerBluetoothService.STATE_CONNECTED -> { }
					ServerBluetoothService.STATE_CONNECTING -> { }
					ServerBluetoothService.STATE_LISTEN, ServerBluetoothService.STATE_NONE -> { }
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
					if (null != activity) {
						Toast.makeText(
								activity,
								readMessage,
								Toast.LENGTH_LONG
						).show()
					}
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
						val client = BluetoothClient(connectedDeviceName)
						clientListAdapter.add(client)
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
