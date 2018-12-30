package br.com.yves.groupmatch.presentation.ui.bluetooth.server

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
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
	private var isDiscoverabilituButtonEnabled = true

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

		setHasOptionsMenu(true)
		setupRecyclerView()

		matchButton.setOnClickListener {
			runOnBackground {
				presenter.onMatchButtonPressed()
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		super.onCreateOptionsMenu(menu, inflater)
		requireActivity().menuInflater.inflate(R.menu.bluetooth_fragment_toolbar_menu, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item!!.itemId) {
			R.id.discoverabilityButton -> runOnBackground {
				presenter.onDiscoverabilityButtonPressed()
			}
		}
		return true
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
			if (clientAdapter.isEmpty()) {
				presenter.onEmptyList()
			}
		}
	}

	override fun toggleProgressBarVisibility(isVisible: Boolean) {
		runOnUiThread {
			progressIndicator.visibility = if (isVisible) VISIBLE else GONE
		}
	}

	//TODO: adicionar um alpha pra ficar claro que o botão está desativado
	override fun toggleDiscoverabilityButton(isEnabled: Boolean) {
		runOnUiThread {
			isDiscoverabilituButtonEnabled = isEnabled
			requireActivity().invalidateOptionsMenu()
		}
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		menu.findItem(R.id.discoverabilityButton)?.isEnabled = isDiscoverabilituButtonEnabled
	}

	override fun sendIntent(intent: Intent, requestCode: Int?) {
		runOnUiThread {
			if (requestCode != null) {
				startActivityForResult(intent, requestCode)
			} else {
				startActivity(intent)
			}
		}
	}

	override fun toggleMatchButtonVisibility(isVisible: Boolean) {
		if (isVisible) matchButton.show() else matchButton.hide()
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

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		runOnBackground {
			presenter.onActivityResult(requestCode, resultCode)
		}
	}

	companion object {
		private val TAG = ::BluetoothServerFragment.name
	}
}