package br.com.yves.groupmatch.domain.loadCalendar

import br.com.yves.groupmatch.domain.models.account.BluetoothUser

interface AccountRepository {
	fun getLoggedUser(): BluetoothUser
}