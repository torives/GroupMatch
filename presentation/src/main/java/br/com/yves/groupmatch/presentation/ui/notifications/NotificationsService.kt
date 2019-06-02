package br.com.yves.groupmatch.presentation.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.auth.GroupMatchApiClient
import br.com.yves.groupmatch.domain.account.AuthenticationService
import br.com.yves.groupmatch.presentation.GroupMatchApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationsService : FirebaseMessagingService() {

	private lateinit var groupMatchService : GroupMatchApiClient
	private lateinit var authService: AuthenticationService

	override fun onNewToken(p0: String?) {
		super.onNewToken(p0)

		p0?.let {
			Log.i(TAG, "New token generated for this InstanceID. Token: $p0")
			sendRegistrationToServer(p0)
		}
	}

	override fun onMessageReceived(p0: RemoteMessage?) {
		super.onMessageReceived(p0)

		p0?.notification?.let { notification ->
			val builder = createNotificationBuilder()
					.setContentTitle(notification.title)
					.setContentText(notification.body)
					.setSmallIcon(R.mipmap.ic_launcher_round)
					.setPriority(NotificationCompat.PRIORITY_DEFAULT)
					.setAutoCancel(true)

			with(NotificationManagerCompat.from(GroupMatchApplication.instance)) {
				notify(999, builder.build())
			}
		}
	}

	private fun createNotificationBuilder(): NotificationCompat.Builder {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			createDefaultNotificationChannel()
		}
		return NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun createDefaultNotificationChannel() {
		val name = getString(R.string.notifications_default_channel_name)
		val channel = NotificationChannel(
				DEFAULT_CHANNEL_ID,
				name,
				NotificationManager.IMPORTANCE_DEFAULT)

		with(getSystemService(NotificationManager::class.java)) {
			createNotificationChannel(channel)
		}
	}

	//TODO: Registrar o token no servidor
	private fun sendRegistrationToServer(token: String) {
		authService.getLoggedUser()?.let { user ->
			groupMatchService.updateDeviceToken(token, user.id)
		}
	}

	companion object {
		private const val DEFAULT_CHANNEL_ID = "default_channel"
		private val TAG = NotificationsService::class.java.simpleName
	}
}