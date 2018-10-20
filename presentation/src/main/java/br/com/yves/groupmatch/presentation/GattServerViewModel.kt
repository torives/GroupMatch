package br.com.yves.groupmatch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData

class GattServerViewModel : ViewModel() {
    var bluetoothDevice: BluetoothDevice? = null
    private var serverName = MutableLiveData<String>()

    fun getName(): LiveData<String> {
        serverName.value = bluetoothDevice?.address ?: ""
        return serverName
    }
}