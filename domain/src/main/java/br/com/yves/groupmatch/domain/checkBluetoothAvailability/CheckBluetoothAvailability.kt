package br.com.yves.groupmatch.domain.checkBluetoothAvailability

import br.com.yves.groupmatch.domain.UseCase

interface BluetoothService {
	fun isBluetoothTurnedOn(): Boolean
	fun isBLESupported(): Boolean
	fun isAdvertisementSupported(): Boolean
}

class CheckBluetoothAvailability(private val bluetoothService: BluetoothService) : UseCase<BluetoohStatus>() {

	override fun execute(): BluetoohStatus {
		return if(bluetoothService.isBluetoothTurnedOn()) {
			if (bluetoothService.isBLESupported()) {
				if(bluetoothService.isAdvertisementSupported()){
					BluetoohStatus.Available
				} else {
					BluetoohStatus.NoAdvertisementSupport
				}
			} else {
				BluetoohStatus.NoBLESupport
			}
		} else {
			BluetoohStatus.TurnedOff
		}
	}
}

enum class BluetoohStatus {
	Available,
	TurnedOff,
	NoBLESupport,
	NoAdvertisementSupport
}