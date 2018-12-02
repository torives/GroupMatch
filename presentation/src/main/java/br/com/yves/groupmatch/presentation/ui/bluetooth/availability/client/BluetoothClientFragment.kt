package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client


import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
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
import br.com.yves.groupmatch.presentation.runOnBackground
import kotlinx.android.synthetic.main.fragment_bluetooth_client.*
import org.jetbrains.anko.support.v4.runOnUiThread

class BluetoothClientFragment : Fragment(), BluetoothView {
	private lateinit var serverAdapter: ServerListAdapter
	private lateinit var presenter: ClientPresenter

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

		presenter = ClientPresenterFactory.createClientPresenter(this)

		foundServersList.layoutManager = LinearLayoutManager(context)
		serverAdapter = ServerListAdapter { server ->
			runOnBackground {
				presenter.onServerSelected(server)
			}
		}
		foundServersList.adapter = serverAdapter
		foundServersList.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)

		fab.setOnClickListener {
			runOnBackground {
				presenter.onClickServerSearchButton()
			}
		}

		runOnBackground {
			presenter.onViewCreated()
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

	//region BluetoothView
	override fun setTitle(resId: Int) {
		runOnUiThread {
			activity?.setTitle(resId)
		}
	}

	override fun setServerSearchButtonImage(resId: Int) {
		runOnUiThread {
			fab.setImageResource(resId)
		}
	}

	override fun toggleProgressBarVisibility(isVisible: Boolean) {
		runOnUiThread {
			progressIndicator.visibility = if (isVisible) VISIBLE else GONE
		}
	}

	override fun displayNewServer(server: BluetoothServer) {
		runOnUiThread {
			serverAdapter.add(server)
		}
	}

	override fun displayNewServers(servers: Collection<BluetoothServer>) {
		runOnUiThread {
			serverAdapter.addAll(servers)
		}
	}

	override fun clearServerList() {
		runOnUiThread {
			serverAdapter.clear()
		}
	}

	override fun showWaitingConnection() {
		runOnUiThread {
			waitingConnectionView.visibility = VISIBLE
		}
	}

	override fun showWaitingMatch() {
		runOnUiThread {
			waitingConnectionView.visibility = GONE
			waitingMatchView.visibility = VISIBLE
		}
	}

	override fun registerBroadcastReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
		runOnUiThread {
			activity?.registerReceiver(receiver, filter)
		}
	}

	override fun unregisterBroadcastReceiver(receiver: BroadcastReceiver) {
		runOnUiThread {
			activity?.unregisterReceiver(receiver)
		}
	}

	override fun displayToast(message: String) {
		runOnUiThread {
			activity?.let {
				Toast.makeText(
						it,
						message,
						Toast.LENGTH_SHORT
				).show()
			}
		}
	}
	//endregion

	companion object {
		private val TAG = BluetoothClientFragment::class.java.simpleName
	}
}