package br.com.yves.groupmatch.data.checkBluetoothAvailability

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import br.com.yves.groupmatch.domain.checkBluetoothAvailability.BluetoothService

class BluetoothServiceImpl(context: Context) : BluetoothService {

	private val bluetoothManager =
		context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager

	private val isBLESupported =
		context.packageManager?.hasSystemFeature(
			PackageManager.FEATURE_BLUETOOTH_LE
		)

	override fun isBluetoothTurnedOn() = bluetoothManager?.adapter?.isEnabled == true

	override fun isBLESupported() = isBLESupported == true

	override fun isAdvertisementSupported() = bluetoothManager?.adapter?.isMultipleAdvertisementSupported == true
}

