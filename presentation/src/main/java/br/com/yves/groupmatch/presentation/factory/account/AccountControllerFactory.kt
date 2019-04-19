package br.com.yves.groupmatch.presentation.factory.account

import br.com.yves.groupmatch.presentation.ui.account.AccountControllerImpl
import br.com.yves.groupmatch.presentation.ui.account.AccountView

object AccountControllerFactory {
	fun create(view: AccountView) = AccountControllerImpl(view)
}