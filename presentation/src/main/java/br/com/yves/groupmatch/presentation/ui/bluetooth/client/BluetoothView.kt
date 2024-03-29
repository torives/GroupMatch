package br.com.yves.groupmatch.presentation.ui.bluetooth.client

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.MatchResultViewModel

interface BluetoothView {
	fun setServerSearchButtonImage(@DrawableRes resId: Int)
	fun toggleProgressBarVisibility(isVisible: Boolean)
	fun displayNewServer(server: BluetoothServer)
	fun displayNewServers(servers: Collection<BluetoothServer>)
	fun showWaitingConnection()
	fun showWaitingMatch()
	fun clearServerList()
	fun registerBroadcastReceiver(receiver: BroadcastReceiver, filter: IntentFilter)
	fun unregisterBroadcastReceiver(receiver: BroadcastReceiver)
	fun resetState()
	fun navigateToMatchResult(result: MatchResultViewModel)

	//DEBUG
	fun displayToast(message: String)

	fun displayToast(@StringRes resId: Int)
}