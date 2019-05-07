package br.com.yves.groupmatch.domain.account

import br.com.yves.groupmatch.domain.models.account.User

interface LoginCallback {
	fun onSuccess(user: User)
	fun onFailure(exception: Exception)
	fun onCanceld()
}