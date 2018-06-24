package br.com.yves.groupmatch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.bluetooth.BluetoothDevice
import android.arch.lifecycle.MutableLiveData

class GattServerViewModel(private val mBluetoothDevice: BluetoothDevice?) : ViewModel() {
    private var serverName = MutableLiveData<String>()

    fun getName(): LiveData<String> {
        serverName.value = when(mBluetoothDevice) {
            null -> ""
            else -> mBluetoothDevice.address
        }
        return serverName
    }
}