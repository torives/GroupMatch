package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.BluetoothClient
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.BluetoothConnectionState
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.BluetoothMessageHandler
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client.ServerBluetoothMessageHandler
import kotlinx.android.synthetic.main.fragment_bluetooth_server.*

interface ServerView {
	fun displayNewClient(client: BluetoothClient)
	fun updateClientStatusIndicator(client: BluetoothClient)
	fun removeClient(client: BluetoothClient)
	fun toggleProgressBarVisibility(isVisible: Boolean)
	fun sendIntent(intent: Intent)


	//DEBUG
	fun displayToast(message: String)
	fun displayToast(@StringRes resId: Int)
}

class ServerPresenter(
		private val view: ServerView,
		private val bluetoothAdapter: BluetoothAdapter
) : BluetoothMessageHandler.Listener {
	private val bluetoothService = ServerBluetoothService(ServerBluetoothMessageHandler(this))

	fun onStart() {
		bluetoothService.start()
	}

	fun onResume() {
		ensureDiscoverable()
	}

	fun onDestroy() {
		bluetoothService.stop()
	}

	//region BluetoothMessageHandler.Listener
	override fun onMessageRead(message: String) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
			BluetoothConnectionState.Idle -> {}
			BluetoothConnectionState.Connecting -> {}
			BluetoothConnectionState.Connected -> {}
			BluetoothConnectionState.Disconnected -> {}
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
}

object ServerPresenterFactory {
	fun create(view: ServerView) = ServerPresenter(view, BluetoothAdapter.getDefaultAdapter())
}

class BluetoothServerFragment : Fragment(), ServerView {
	private lateinit var clientAdapter: ClientAdapter
	private lateinit var presenter: ServerPresenter

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

		presenter = ServerPresenterFactory.create(this)

		foundServersList.layoutManager = LinearLayoutManager(context)
		clientAdapter = ClientAdapter()
		foundServersList.adapter = clientAdapter
		foundServersList.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)
	}

	override fun onStart() {
		super.onStart()
		runOnBackground {
			presenter.onStart()
		}
	}

	override fun onResume() {
		super.onResume()
		runOnBackground {
			presenter.onResume()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		runOnBackground {
			presenter.onDestroy()
		}
	}
	//endregion

	//region ServerView
	override fun displayNewClient(client: BluetoothClient) {
		runOnUiThread {
			clientAdapter.add(client)
		}
	}

	override fun updateClientStatusIndicator(client: BluetoothClient) {
		runOnUiThread {
			clientAdapter.update(client)
		}
	}

	override fun removeClient(client: BluetoothClient) {
		runOnUiThread {
			clientAdapter.remove(client)
		}
	}

	override fun toggleProgressBarVisibility(isVisible: Boolean) {
		runOnUiThread {
			progressIndicator.visibility = if (isVisible) VISIBLE else GONE
		}
	}

	override fun sendIntent(intent: Intent) {
		runOnUiThread {
			startActivity(intent)
		}
	}

	override fun displayToast(message: String) {
		runOnUiThread {
			Toast.makeText(
					it,
					message,
					Toast.LENGTH_SHORT
			).show()
		}
	}

	override fun displayToast(@StringRes resId: Int) {
		runOnUiThread {
			val message = resources.getString(resId)
			displayToast(message)
		}
	}
	//endregion

	companion object {
		private val TAG = ::BluetoothServerFragment.name
	}
}