package br.com.yves.groupmatch.presentation

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GattServerViewModel : ViewModel() {
	var bluetoothDevice: BluetoothDevice? = null
	private var serverName = MutableLiveData<String>()

	fun getName(): LiveData<String> {
		serverName.value = bluetoothDevice?.address ?: ""
		return serverName
	}
}