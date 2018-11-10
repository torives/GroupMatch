package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.*
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.BluetoothServerFragment.Companion.TAG
import br.com.yves.groupmatch.presentation.ui.bluetooth.availability.server.SERVICE_UUID
import kotlinx.android.synthetic.main.fragment_search_bluetooth_server.*
import kotlinx.android.synthetic.main.view_log.view.*
import kotlinx.android.synthetic.main.widget_server_list_item.view.*
import java.util.*


const val SCAN_PERIOD: Long = 5000

class BluetoothClientFragment : Fragment() {

	private val TAG = "ClientActivity"

	private val REQUEST_ENABLE_BT = 1
	private val REQUEST_FINE_LOCATION = 2

	private var mScanning: Boolean = false
	private var mHandler: Handler? = null
	private var mLogHandler: Handler = Handler(Looper.getMainLooper())
	private var mScanResults: Map<String, BluetoothDevice>? = null

	private var mConnected: Boolean = false
	private var mEchoInitialized: Boolean = false
	private var mBluetoothAdapter: BluetoothAdapter? = null
	private var mBluetoothLeScanner: BluetoothLeScanner? = null
	private var mScanCallback: BtleScanCallback? = null
	private var mGatt: BluetoothGatt? = null

	//Lifecycle

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_search_bluetooth_server, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

		@SuppressLint("HardwareIds")
		val deviceInfo =
			"Device Info \nName: ${mBluetoothAdapter?.name} \nAddress: ${mBluetoothAdapter?.address}"
		clientDeviceInfoTextView.text = deviceInfo

		startScanningButton.setOnClickListener { startScan() }
		stopScanningButton.setOnClickListener { stopScan() }
		sendMessageButton.setOnClickListener { sendMessage() }
		disconnectButton.setOnClickListener { disconnectGattServer() }
		clientLogView.clearLogButton.setOnClickListener { clearLogs() }
	}

	override fun onResume() {
		super.onResume()
		// Check low energy support
	}

	// Scanning

	private fun startScan() {
		if (!hasPermissions() || mScanning) {
			return
		}

		disconnectGattServer()

		mScanResults = HashMap()
		mScanCallback =
				BtleScanCallback(
					clientLogView
				)
		mScanCallback!!.scanResults = mScanResults as MutableMap<String, BluetoothDevice>
		mBluetoothLeScanner = mBluetoothAdapter?.bluetoothLeScanner

		// Note: Filtering does not work the same (or at all) on most devices. It also is unable to
		// search for a mask or anything less than a full UUID.
		// Unless the full UUID of the server is known, manual filtering may be necessary.
		// For example, when looking for a brand of device that contains a char sequence in the UUID
		val scanFilter = ScanFilter.Builder().setServiceUuid(ParcelUuid(SERVICE_UUID)).build()
		val filters = ArrayList<ScanFilter>()
		filters.add(scanFilter)

		val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()


		mBluetoothLeScanner?.startScan(filters, settings, mScanCallback)

		mHandler = Handler()
		mHandler?.postDelayed(
			{ this.stopScan() },
			SCAN_PERIOD
		)

		mScanning = true
		log("Started scanning.")
	}

	private fun stopScan() {
		if (mScanning && mBluetoothAdapter != null && mBluetoothAdapter!!.isEnabled && mBluetoothLeScanner != null) {
			mBluetoothLeScanner!!.stopScan(mScanCallback)
			scanComplete()
		}

		mScanCallback = null
		mScanning = false
		mHandler = null
		log("Stopped scanning.")
	}

	private fun scanComplete() {
		if (mScanResults!!.isEmpty()) {
			return
		}

		for (deviceAddress in mScanResults!!.keys) {
			val device = mScanResults!![deviceAddress]

			val layout = serverListTextView.parent as LinearLayout
			val index = layout.indexOfChild(serverListTextView)
			val widget = ServerListItemWidget(context!!)

			layout.addView(widget, index + 1)

			val model = ViewModelProviders.of(this).get(GattServerViewModel::class.java)
			model.bluetoothDevice = device
			model.getName().observe(this, Observer {
				widget.serverNameTextView.text = it
			})
			widget.connectToServerButton.setOnClickListener { connectDevice(device!!) }
		}
	}

	private fun hasPermissions(): Boolean {
		if (mBluetoothAdapter == null || mBluetoothAdapter?.isEnabled == false) {
			requestBluetoothEnable()
			return false
		} else if (!hasLocationPermissions()) {
			requestLocationPermission()
			return false
		}
		return true
	}

	private fun requestBluetoothEnable() {
		val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
		startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
		log("Requested user enables Bluetooth. Try starting the scan again.")
	}

	private fun hasLocationPermissions(): Boolean {
		return checkSelfPermission(
			context!!,
			Manifest.permission.ACCESS_FINE_LOCATION
		) == PackageManager.PERMISSION_GRANTED
	}

	private fun requestLocationPermission() {
		requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
		log("Requested user enable Location. Try starting the scan again.")
	}


	// Gatt bluetooth

	private fun connectDevice(device: BluetoothDevice) {
		log("Connecting to " + device.address)
		mGatt = device.connectGatt(context!!, false, object : BluetoothGattCallback() {

			override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
				super.onConnectionStateChange(gatt, status, newState)
				log("onConnectionStateChange newState: $newState")

				if (status == BluetoothGatt.GATT_FAILURE) {
					logError("Connection Gatt failure status $status")
					disconnectGattServer()
					return
				} else if (status != BluetoothGatt.GATT_SUCCESS) {
					// handle anything not SUCCESS as failure
					logError("Connection not GATT sucess status $status")
					disconnectGattServer()
					return
				}

				if (newState == BluetoothProfile.STATE_CONNECTED) {
					log("Connected to device " + gatt.device.address)
					setConnected(true)
					gatt.discoverServices()
				} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
					log("Disconnected from device")
					disconnectGattServer()
				}
			}

			override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
				super.onServicesDiscovered(gatt, status)

				if (status != BluetoothGatt.GATT_SUCCESS) {
					log("Device service discovery unsuccessful, status $status")
					return
				}

				val matchingCharacteristics =
					BluetoothUtils.findCharacteristics(
						gatt
					)
				if (matchingCharacteristics.isEmpty()) {
					logError("Unable to find characteristics.")
					return
				}

				log("Initializing: setting write type and enabling notification")
				for (characteristic in matchingCharacteristics) {
					characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
					enableCharacteristicNotification(gatt, characteristic)
				}
			}

			override fun onCharacteristicWrite(
				gatt: BluetoothGatt,
				characteristic: BluetoothGattCharacteristic,
				status: Int
			) {
				super.onCharacteristicWrite(gatt, characteristic, status)
				if (status == BluetoothGatt.GATT_SUCCESS) {
					log("Characteristic written successfully")
				} else {
					logError("Characteristic write unsuccessful, status: $status")
					disconnectGattServer()
				}
			}

			override fun onCharacteristicRead(
				gatt: BluetoothGatt,
				characteristic: BluetoothGattCharacteristic,
				status: Int
			) {
				super.onCharacteristicRead(gatt, characteristic, status)
				if (status == BluetoothGatt.GATT_SUCCESS) {
					log("Characteristic read successfully")
					readCharacteristic(characteristic)
				} else {
					logError("Characteristic read unsuccessful, status: $status")
					// Trying to read from the Time Characteristic? It doesnt have the property or permissions
					// set to allow this. Normally this would be an error and you would want to:
					// disconnectGattServer();
				}
			}

			override fun onCharacteristicChanged(
				gatt: BluetoothGatt,
				characteristic: BluetoothGattCharacteristic
			) {
				super.onCharacteristicChanged(gatt, characteristic)
				log("Characteristic changed, " + characteristic.uuid.toString())
				readCharacteristic(characteristic)
			}

			private fun enableCharacteristicNotification(
				gatt: BluetoothGatt,
				characteristic: BluetoothGattCharacteristic
			) {
				val characteristicWriteSuccess =
					gatt.setCharacteristicNotification(characteristic, true)
				if (characteristicWriteSuccess) {
					log("Characteristic notification set successfully for " + characteristic.uuid.toString())
					if (BluetoothUtils.isEchoCharacteristic(
							characteristic
						)
					) {
						initializeEcho()
					}
				} else {
					logError("Characteristic notification set failure for " + characteristic.uuid.toString())
				}
			}

			private fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
				val messageBytes = characteristic.value
				log(
					"Read: " + StringUtils.byteArrayInHexFormat(
						messageBytes
					)
				)
				val message =
					StringUtils.stringFromBytes(messageBytes)
				if (message == null) {
					logError("Unable to convert bytes to string")
					return
				}

				log("Received message: $message")
			}
		})
	}

	// Messaging

	private fun sendMessage() {
		if (!mConnected || !mEchoInitialized) {
			return
		}

		val characteristic =
			BluetoothUtils.findEchoCharacteristic(
				mGatt!!
			)
		if (characteristic == null) {
			logError("Unable to find echo characteristic.")
			disconnectGattServer()
			return
		}

//        val message = mBinding.messageEditText.getText().toString()
		log("Sending message: TRETA")

		val messageBytes = StringUtils.bytesFromString("TRETA")
		if (messageBytes.isEmpty()) {
			logError("Unable to convert message to bytes")
			return
		}

		characteristic!!.setValue(messageBytes)
		val success = mGatt!!.writeCharacteristic(characteristic)
		if (success) {
			log(
				"Wrote: " + StringUtils.byteArrayInHexFormat(
					messageBytes
				)
			)
		} else {
			logError("Failed to write data")
		}
	}

	// Logging

	private fun clearLogs() {
		mLogHandler.post { clientLogView.logTextView.text = "" }
	}

	fun log(msg: String) {
		Log.d(TAG, msg)
		mLogHandler.post {
			clientLogView.logTextView.append(msg + "\n")
			clientLogView.logScrollView.post { clientLogView.logScrollView.fullScroll(View.FOCUS_DOWN) }
		}
	}

	fun logError(msg: String) {
		log("Error: $msg")
	}

	// Gat Client Actions

	fun setConnected(connected: Boolean) {
		mConnected = connected
	}

	fun initializeEcho() {
		mEchoInitialized = true
	}

	fun disconnectGattServer() {
		log("Closing Gatt bluetooth")
		clearLogs()
		mConnected = false
		mEchoInitialized = false

		mGatt?.disconnect()
		mGatt?.close()
	}

	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment BluetoothClientFragment.
		 */
		// TODO: Rename and change types and number of parameters
		@JvmStatic
		fun newInstance() =
			BluetoothClientFragment()
	}
}

class BtleScanCallback(
	var scanResults: MutableMap<String, BluetoothDevice>? = null,
	private var logView: View?
) : ScanCallback() {
	constructor(logView: View?) : this(null, logView)

	private val mLogHandler = Handler(Looper.getMainLooper())

	override fun onScanResult(callbackType: Int, result: ScanResult) {
		addScanResult(result)
	}

	override fun onBatchScanResults(results: List<ScanResult>) {
		for (result in results) {
			addScanResult(result)
		}
	}

	override fun onScanFailed(errorCode: Int) {
		logError("BLE Scan Failed with code $errorCode")
	}

	private fun addScanResult(result: ScanResult) {
		val device = result.device
		val deviceAddress = device.address
		scanResults?.let {
			it[deviceAddress] = device
		}
	}

	private fun log(msg: String) {
		Log.d(TAG, msg)
		mLogHandler.post {
			logView?.findViewById<TextView>(R.id.logTextView)?.append(msg + "\n")
			val logScrollView = logView?.findViewById<ScrollView>(R.id.logScrollView)
			logScrollView?.post { logScrollView.fullScroll(View.FOCUS_DOWN) }
		}
	}

	private fun logError(msg: String) {
		log("Error: $msg")
	}
}
