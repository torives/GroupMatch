package br.com.yves.groupmatch.presentation.ui.bluetooth.server

import android.content.Intent
import androidx.annotation.StringRes
import br.com.yves.groupmatch.presentation.ui.bluetooth.client.BluetoothClient

interface ServerView {
	fun displayNewClient(client: BluetoothClient)
	fun updateClientStatusIndicator(client: BluetoothClient)
	fun removeClient(client: BluetoothClient)
	fun toggleProgressBarVisibility(isVisible: Boolean)
	fun sendIntent(intent: Intent)
	fun toggleMatchButtonVisibility(isVisible: Boolean)

	//DEBUG
	fun displayToast(message: String)
	fun displayToast(@StringRes resId: Int)
}