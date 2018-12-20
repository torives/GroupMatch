package br.com.yves.groupmatch.presentation.ui.bluetooth.server

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
import br.com.yves.groupmatch.presentation.ui.bluetooth.client.BluetoothClient
import kotlinx.android.synthetic.main.fragment_bluetooth_server.*

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

		setupRecyclerView()

		matchButton.setOnClickListener {
			runOnBackground {
				presenter.onMatchButtonPressed()
			}
		}
	}

	private fun setupRecyclerView() {
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

			//FIXME: Thou shall not write logic on the view
			if(clientAdapter.isEmpty()) {
				presenter.onEmptyList()
			}
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

	override fun toggleMatchButtonVisibility(isVisible: Boolean) {
		matchButton.visibility = if(isVisible) VISIBLE else GONE
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