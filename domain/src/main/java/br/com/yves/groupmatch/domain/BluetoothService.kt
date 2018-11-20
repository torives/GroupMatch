package br.com.yves.groupmatch.domain

interface BluetoothService {
	fun isBluetoothAvailable(): Boolean
	fun isBluetoothEnabled(): Boolean
	fun isLocationPermissionGranted(): Boolean
}