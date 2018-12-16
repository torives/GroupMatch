package br.com.yves.groupmatch.presentation.ui.bluetooth.server

import android.bluetooth.BluetoothAdapter

object ServerPresenterFactory {
	fun create(view: ServerView) = ServerPresenter(view, BluetoothAdapter.getDefaultAdapter())
}