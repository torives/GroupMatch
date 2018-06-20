package br.com.yves.groupmatch.scenes.connectionRole

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import java.lang.ref.WeakReference

class ConnectionRoleInteractor constructor(view: ConnectionRoleView): LifecycleObserver {

    private var mView: WeakReference<ConnectionRoleView> = WeakReference(view)
    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null

    init {
        mBluetoothManager = mView.get()?.getBluetoothManager()
        mBluetoothAdapter = mBluetoothManager?.adapter
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun viewResumed() {
        // Check if bluetooth is enabled
        if (mBluetoothAdapter?.isEnabled == false) {
            // Request user to enable it
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBtIntent)
            return
        }

        // Check low energy support
        if (activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) == false) {
            // Get a newer device
//            log("No LE Support.")
            activity?.finish()
            return
        }

        // Check advertising
        if (mBluetoothAdapter?.isMultipleAdvertisementSupported == false) {
            // Unable to run the server on this device, get a better device
//            log("No Advertising Support.")
            activity?.finish()
            return
        }
    }
}