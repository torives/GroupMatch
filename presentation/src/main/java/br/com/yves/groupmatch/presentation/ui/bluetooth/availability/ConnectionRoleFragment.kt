package br.com.yves.groupmatch.presentation.ui.bluetooth.availability

import android.Manifest
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
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.BluetoothAvailabilityView.Companion.ENABLE_BLUETOOTH_REQUEST
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.BluetoothAvailabilityView.Companion.LOCATION_PERMISSION_REQUEST
import kotlinx.android.synthetic.main.fragment_connection_role.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton


class ConnectionRoleFragment : Fragment(),
	BluetoothAvailabilityView {

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
		positiveCallback: (() -> Unit)?,
		negativeCallback: (() -> Unit)?
	) {
		activity?.runOnUiThread {
			alert(message, title) {
				positiveCallback?.let { yesButton { positiveCallback.invoke() } }
				negativeCallback?.let { noButton { negativeCallback.invoke() } }
			}.apply {
				isCancelable = false
				show()
			}
		}
	}

	override fun displayBluetoothActivationDialog() {
		activity?.runOnUiThread {
			val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
			startActivityForResult(
				enableBtIntent,
				ENABLE_BLUETOOTH_REQUEST
			)
		}
	}

	override fun displayLocationPermissionDialog() {
		activity?.runOnUiThread {
			requestPermissions(
				arrayOf(
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION
				), LOCATION_PERMISSION_REQUEST
			)
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

	//FIXME: Tratar fluxos de erro
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		runOnBackground {
			when (requestCode) {
				ENABLE_BLUETOOTH_REQUEST -> presenter.onBluetoothActivationDialogResult(resultCode)
				LOCATION_PERMISSION_REQUEST -> presenter.onLocationPermissionGiven()
			}
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	companion object {
//		const val ENABLE_BLUETOOTH_REQUEST = 100
//		const val ENABLE_BLUETOOTH_RESPONSE_ALLOW = -1
//		const val ENABLE_BLUETOOTH_RESPONSE_DENY = 0
//		const val LOCATION_PERMISSION_REQUEST = 200
	}
}