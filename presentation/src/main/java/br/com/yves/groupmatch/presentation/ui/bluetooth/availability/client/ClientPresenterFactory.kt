package br.com.yves.groupmatch.presentation.ui.bluetooth.availability.client

import android.bluetooth.BluetoothAdapter

object ClientPresenterFactory {
	fun createClientPresenter(view: BluetoothView) = ClientPresenter(view, BluetoothAdapter.getDefaultAdapter())
}