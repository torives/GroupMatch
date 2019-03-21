package br.com.yves.groupmatch.presentation.ui.notifications

import com.google.firebase.messaging.FirebaseMessagingService

class NotificationsService : FirebaseMessagingService() {
	override fun onNewToken(p0: String?) {
		super.onNewToken(p0)

		p0?.let {
			sendRegistrationToServer(p0)
		}
	}

	//TODO: Registrar o token no servidor
	private fun sendRegistrationToServer(token: String) {

	}
}