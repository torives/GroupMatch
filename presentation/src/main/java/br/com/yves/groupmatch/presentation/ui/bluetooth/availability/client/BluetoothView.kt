package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface BluetoothView {
	fun setTitle(@StringRes resId: Int)
	fun setServerSearchButtonImage(@DrawableRes resId: Int)
	fun toggleProgressBarVisibility(isVisible: Boolean)
	fun displayNewServer(server: BluetoothServer)
	fun displayNewServers(servers: Collection<BluetoothServer>)
	fun showWaitingConnection()
	fun showWaitingMatch()
	fun clearServerList()
	fun registerBroadcastReceiver(receiver: BroadcastReceiver, filter: IntentFilter)
	fun unregisterBroadcastReceiver(receiver: BroadcastReceiver)


	//DEBUG
	fun displayToast(message: String)
}