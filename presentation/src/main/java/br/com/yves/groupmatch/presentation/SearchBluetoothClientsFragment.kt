package br.com.yves.groupmatch.presentation


import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.fragment_search_bluetooth_clients.*
import kotlinx.android.synthetic.main.view_log.*
import kotlinx.android.synthetic.main.view_log.view.*
import java.util.*


const val SERVICE_STRING = "7D2EA28A-F7BD-485A-BD9D-92AD6ECFE93E"
const val CHARACTERISTIC_ECHO_STRING = SERVICE_STRING
val SERVICE_UUID: UUID = UUID.fromString(SERVICE_STRING)
val CHARACTERISTIC_ECHO_UUID: UUID = UUID.fromString(CHARACTERISTIC_ECHO_STRING)

class SearchBluetoothClientsFragment : Fragment() {

	private var mBluetoothManager: BluetoothManager? = null //FIXME: Receber por parâmetro?
	private var mBluetoothAdapter: BluetoothAdapter? = null //FIXME: Receber por parâmetro?

	private var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
	private var mGattServer: BluetoothGattServer? = null
	private var mAdvertiseCallback: AdvertiseCallback? = null

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

		mAdvertiseCallback = object : AdvertiseCallback() {
			override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
				log("Peripheral advertising started.")
			}

			override fun onStartFailure(errorCode: Int) {
				log("Peripheral advertising failed: $errorCode")
			}
		}

		restartServerButton.setOnClickListener { restartServer() }
		clearLogButton.setOnClickListener { clearLogs() }

	}

	override fun onResume() {
		super.onResume()

		mBluetoothLeAdvertiser = mBluetoothAdapter?.bluetoothLeAdvertiser
		mGattServer =
				mBluetoothManager?.openGattServer(context, object : BluetoothGattServerCallback() {

					override fun onConnectionStateChange(
						device: BluetoothDevice,
						status: Int,
						newState: Int
					) {
						super.onConnectionStateChange(device, status, newState)
						log("onConnectionStateChange " + device.address + "\nstatus " + status + "\nnewState " + newState)

						if (newState == BluetoothProfile.STATE_CONNECTED) {
							addDevice(device)
						} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
							removeDevice(device)
						}
					}

					// The Gatt will reject Characteristic Read requests that do not have the permission set,
					// so there is no need to check inside the callback
					override fun onCharacteristicReadRequest(
						device: BluetoothDevice,
						requestId: Int,
						offset: Int,
						characteristic: BluetoothGattCharacteristic
					) {
						super.onCharacteristicReadRequest(device, requestId, offset, characteristic)

						log("onCharacteristicReadRequest " + characteristic.uuid.toString())

						if (BluetoothUtils.requiresResponse(characteristic)) {
							// Unknown read characteristic requiring response, send failure
							sendResponse(device, requestId, BluetoothGatt.GATT_FAILURE, 0, null)
						}
						// Not one of our characteristics or has NO_RESPONSE property set
					}

					// The Gatt will reject Characteristic Write requests that do not have the permission set,
					// so there is no need to check inside the callback
					override fun onCharacteristicWriteRequest(
						device: BluetoothDevice,
						requestId: Int,
						characteristic: BluetoothGattCharacteristic,
						preparedWrite: Boolean,
						responseNeeded: Boolean,
						offset: Int,
						value: ByteArray
					) {
						super.onCharacteristicWriteRequest(
							device,
							requestId,
							characteristic,
							preparedWrite,
							responseNeeded,
							offset,
							value
						)
						log(
							"onCharacteristicWriteRequest" + characteristic.uuid.toString() + "\nReceived: " + StringUtils.byteArrayInHexFormat(
								value
							)
						)

						if (CHARACTERISTIC_ECHO_UUID.equals(characteristic.uuid)) {
							sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
							sendReverseMessage(value)
						}
					}

					override fun onNotificationSent(device: BluetoothDevice, status: Int) {
						super.onNotificationSent(device, status)
						log("onNotificationSent")
					}
				})

		@SuppressLint("HardwareIds")
		val deviceInfo =
			"Device Info \nName: ${mBluetoothAdapter?.name} \nAddress: ${mBluetoothAdapter?.address}"
		serverDeviceInfoTextView.text = deviceInfo

		setupServer()
		startAdvertising()
	}

	// GattServer

	private fun setupServer() {
		val service = BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY)

		// Write characteristic
		val writeCharacteristic = BluetoothGattCharacteristic(
			SERVICE_UUID, BluetoothGattCharacteristic.PROPERTY_WRITE,
			// Somehow this is not necessary, the client can still enable notifications
			//                        | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
			BluetoothGattCharacteristic.PERMISSION_WRITE
		)

		service.addCharacteristic(writeCharacteristic)

		mGattServer?.addService(service)
	}

	private fun stopServer() {
		if (mGattServer != null) {
			mGattServer?.close()
		}
	}

	private fun restartServer() {
		stopAdvertising()
		stopServer()
		setupServer()
		startAdvertising()
	}

	// Advertising

	private fun startAdvertising() {
		mBluetoothLeAdvertiser?.let {

			val settings = AdvertiseSettings.Builder()
				.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED).setConnectable(true)
				.setTimeout(0).setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW).build()

			val parcelUuid = ParcelUuid(SERVICE_UUID)
			val data =
				AdvertiseData.Builder().setIncludeDeviceName(false).addServiceUuid(parcelUuid)
					.build()

			it.startAdvertising(settings, data, mAdvertiseCallback)
		}
	}

	private fun stopAdvertising() {
		mBluetoothLeAdvertiser?.stopAdvertising(mAdvertiseCallback)
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

	// Notifications

	private fun notifyCharacteristic(value: ByteArray, uuid: UUID) {
		mHandler.post {
			val service = mGattServer!!.getService(SERVICE_UUID)
			val characteristic = service.getCharacteristic(uuid)
			log(
				"Notifying characteristic " + characteristic?.uuid.toString() + ", new value: " + StringUtils.byteArrayInHexFormat(
					value
				)
			)

			characteristic.value = value
			val confirm = BluetoothUtils.requiresConfirmation(characteristic)
			for (device in mDevices) {
				mGattServer?.notifyCharacteristicChanged(device, characteristic, confirm)
			}
		}
	}

	// Gatt Server Action Listener

	fun addDevice(device: BluetoothDevice) {
		log("Deviced added: " + device.address)
		mHandler.post { mDevices.add(device) }
	}

	fun removeDevice(device: BluetoothDevice) {
		log("Deviced removed: " + device.address)
		mHandler.post { mDevices.remove(device) }
	}

	fun sendResponse(
		device: BluetoothDevice,
		requestId: Int,
		status: Int,
		offset: Int,
		value: ByteArray?
	) {
		mHandler.post { mGattServer?.sendResponse(device, requestId, status, 0, null) }
	}

	private fun sendReverseMessage(message: ByteArray) {
		mHandler.post {
			// Reverse message to differentiate original message & response
			val response = ByteUtils.reverse(message)
			log("Sending: " + StringUtils.byteArrayInHexFormat(response))
			notifyCharacteristicEcho(response)
		}
	}

	private fun notifyCharacteristicEcho(value: ByteArray) {
		notifyCharacteristic(value, CHARACTERISTIC_ECHO_UUID)
	}

	companion object {

		val TAG: String = ::SearchBluetoothClientsFragment.name
		@JvmStatic
		fun newInstance() = SearchBluetoothClientsFragment()
	}
}
