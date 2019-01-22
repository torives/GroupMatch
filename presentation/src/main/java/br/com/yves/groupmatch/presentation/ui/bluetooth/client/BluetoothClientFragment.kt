package br.com.yves.groupmatch.presentation.ui.bluetooth.client


import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import kotlinx.android.synthetic.main.fragment_bluetooth_client.*

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
			fab.hide()
			waitingConnectionView.visibility = VISIBLE
		}
	}

	override fun showWaitingMatch() {
		runOnUiThread {
			fab.hide()
			waitingConnectionView.visibility = GONE
			waitingMatchView.visibility = VISIBLE
		}
	}

	override fun registerBroadcastReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
		runOnUiThread {
			it.registerReceiver(receiver, filter)
		}
	}

	override fun unregisterBroadcastReceiver(receiver: BroadcastReceiver) {
		runOnUiThread {
			it.unregisterReceiver(receiver)
		}
	}

	override fun resetState() {
		runOnUiThread {
			waitingMatchView.visibility = GONE
			waitingConnectionView.visibility = GONE
			fab.show()
			serverAdapter.clear()
		}
	}

	override fun navigateToMatchResult(result: MatchResult) {
		findNavController().navigate(R.id.matchResultFragment)
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
		private val TAG = BluetoothClientFragment::class.java.simpleName
	}
}