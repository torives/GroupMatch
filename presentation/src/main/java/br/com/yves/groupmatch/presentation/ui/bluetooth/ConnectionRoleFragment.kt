package br.com.yves.groupmatch.presentation.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.factory.checkBluetoothAvailability.BluetoothAvailabilityPresenterFactory
import br.com.yves.groupmatch.presentation.runOnBackground
import kotlinx.android.synthetic.main.fragment_connection_role.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton

class ConnectionRoleFragment : Fragment(), BluetoothAvailabilityView {

	private var presenter = BluetoothAvailabilityPresenterFactory.create(this)

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_connection_role, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupOnClickListeners()
		runOnBackground {
			presenter.onViewCreated()
		}
	}

	private fun setupOnClickListeners() {
		serverButton.setOnClickListener {
			runOnBackground {
				presenter.onServerButtonClicked()
			}
		}
		clientButton.setOnClickListener {
			runOnBackground {
				presenter.onClientButtonClicked()
			}
		}
	}

	//region BluetoothAvailabilityView
	override fun displayErrorDialog(
		title: String,
		message: String,
		callback: ((isAnswerPositive: Boolean) -> Unit)?
	) {
		activity?.runOnUiThread {
			alert(message, title) {
				yesButton { callback?.invoke(true) }
				noButton { callback?.invoke(false) }
			}.show()
		}
	}

	override fun navigateToBluetoothOptions() {
		activity?.runOnUiThread {
			val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
			startActivityForResult(enableBtIntent, 100)
		}
	}

	override fun navigateToCalendarView() {
		activity?.runOnUiThread {
			val navController = NavHostFragment.findNavController(this)
			navController.navigate(R.id.calendarFragment)
		}
	}

	override fun navigateToBluetoothServerView() {
		activity?.runOnUiThread {
			val navController = NavHostFragment.findNavController(this)
			navController.navigate(R.id.action_connectionRoleFragment_to_searchBluetoothClientsFragment)
		}
	}

	override fun navigateToBluetoothClientView() {
		activity?.runOnUiThread {
			val navController = NavHostFragment.findNavController(this)
			navController.navigate(R.id.action_connectionRoleFragment_to_searchBluetoothServer)
		}
	}
	//endregion


	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == 100) {
			toast("Habilitou")
		}
		super.onActivityResult(requestCode, resultCode, data)
	}
}