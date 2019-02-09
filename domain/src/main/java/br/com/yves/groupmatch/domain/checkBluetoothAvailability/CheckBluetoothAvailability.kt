package br.com.yves.groupmatch.domain.checkBluetoothAvailability

import br.com.yves.groupmatch.domain.BluetoothService
import br.com.yves.groupmatch.domain.UseCase

class CheckBluetoothAvailability(private val bluetoothService: BluetoothService) : UseCase<BluetoothStatus>() {

	override fun execute(): BluetoothStatus {
		return if(bluetoothService.isBluetoothAvailable()) {
			if (bluetoothService.isBluetoothEnabled()) {
				if(bluetoothService.isLocationPermissionGranted()){
					BluetoothStatus.Available
				} else {
					BluetoothStatus.NoLocationPermission
				}
			} else {
				BluetoothStatus.Disabled
			}
		} else {
			BluetoothStatus.Unsupported
		}
	}
}

