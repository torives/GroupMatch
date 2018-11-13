package br.com.yves.groupmatch.domain.checkBluetoothAvailability

import br.com.yves.groupmatch.domain.BluetoothService
import br.com.yves.groupmatch.domain.UseCase

class CheckBluetoothAvailability(private val bluetoothService: BluetoothService) : UseCase<BluetoothStatus>() {

	override fun execute(): BluetoothStatus {
		return if(bluetoothService.isBluetoothTurnedOn()) {
			if (bluetoothService.isBLESupported()) {
				if(bluetoothService.isAdvertisementSupported()){
					BluetoothStatus.Available
				} else {
					//BluetoothStatus.NoAdvertisementSupport
					BluetoothStatus.Available
				}
			} else {
				BluetoothStatus.NoBLESupport
			}
		} else {
			BluetoothStatus.TurnedOff
		}
	}
}

enum class BluetoothStatus {
	Available,
	TurnedOff,
	NoBLESupport,
	NoAdvertisementSupport
}