package br.com.yves.groupmatch.data

import android.bluetooth.BluetoothManager
import android.content.Context
import br.com.yves.groupmatch.domain.loadCalendar.AccountRepository
import br.com.yves.groupmatch.domain.models.account.User

class UserRepo(context: Context) : AccountRepository {
	private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

	override fun getLoggedUser(): User {
		return User(bluetoothManager.adapter.name)
	}
}