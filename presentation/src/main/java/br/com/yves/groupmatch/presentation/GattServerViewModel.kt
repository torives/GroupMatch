package br.com.yves.groupmatch.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.bluetooth.BluetoothDevice
import android.arch.lifecycle.MutableLiveData

class GattServerViewModel : ViewModel() {
    var bluetoothDevice: BluetoothDevice? = null
    private var serverName = MutableLiveData<String>()

    fun getName(): LiveData<String> {
        serverName.value = bluetoothDevice?.address ?: ""
        return serverName
    }
}