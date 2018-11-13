package br.com.yves.groupmatch.data.checkBluetoothAvailability

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.PermissionChecker.checkSelfPermission
import br.com.yves.groupmatch.domain.BluetoothService

class BluetoothServiceImpl(context: Context) : BluetoothService {

	private val context = context.applicationContext

	private val bluetoothManager =
		this.context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager

	override fun isBluetoothTurnedOn() = bluetoothManager?.adapter?.isEnabled == true

	override fun isBLESupported() = context.packageManager?.hasSystemFeature(
		PackageManager.FEATURE_BLUETOOTH_LE
	) == true

	override fun isAdvertisementSupported() =
		bluetoothManager?.adapter?.isMultipleAdvertisementSupported == true

	override fun isLocationPermissionGranted() =
		checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

