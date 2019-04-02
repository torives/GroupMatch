package br.com.yves.groupmatch.presentation.ui.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class NotificationsService : FirebaseMessagingService() {
	override fun onNewToken(p0: String?) {
		super.onNewToken(p0)

		p0?.let {
			Log.i(TAG, "New token generated for this InstanceID. Token: $p0")
			sendRegistrationToServer(p0)
		}
	}

	//TODO: Registrar o token no servidor
	private fun sendRegistrationToServer(token: String) {

	}

	companion object {
		private val TAG = NotificationsService::class.java.simpleName
	}
}