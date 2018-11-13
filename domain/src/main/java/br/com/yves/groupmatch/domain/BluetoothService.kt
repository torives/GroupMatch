package br.com.yves.groupmatch.domain

interface BluetoothService {
	fun isBluetoothTurnedOn(): Boolean
	fun isBLESupported(): Boolean
	fun isAdvertisementSupported(): Boolean
	fun isLocationPermissionGranted(): Boolean
}