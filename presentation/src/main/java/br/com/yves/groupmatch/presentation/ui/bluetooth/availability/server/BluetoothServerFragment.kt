package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.fragment_search_bluetooth_clients.*
import kotlinx.android.synthetic.main.view_log.view.*
import java.util.*


const val SERVICE_STRING = "7D2EA28A-F7BD-485A-BD9D-92AD6ECFE93E"
val SERVICE_UUID: UUID = UUID.fromString(SERVICE_STRING)

class BluetoothServerFragment : Fragment() {

	private var mBluetoothManager: BluetoothManager? = null //FIXME: Receber por parâmetro?
	private var mBluetoothAdapter: BluetoothAdapter? = null //FIXME: Receber por parâmetro?

	private val mHandler: Handler = Handler()
	private val mLogHandler: Handler = Handler(Looper.getMainLooper())
	private val mDevices = ArrayList<BluetoothDevice>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_search_bluetooth_clients, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		mBluetoothManager =
				activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
		mBluetoothAdapter = mBluetoothManager?.adapter
	}

	override fun onResume() {
		super.onResume()
	}

	// Logging

	fun log(msg: String) {
		Log.d(TAG, msg)
		mLogHandler.post {
			serverLogView.logTextView.append(msg + "\n")
			serverLogView.logScrollView.post { serverLogView.logScrollView.fullScroll(View.FOCUS_DOWN) }
		}
	}

	private fun clearLogs() {
		mLogHandler.post { serverLogView.logTextView.text = "" }
	}

	companion object {
		val TAG: String = ::BluetoothServerFragment.name
		@JvmStatic
		fun newInstance() =
			BluetoothServerFragment()
	}
}
