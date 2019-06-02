package br.com.yves.groupmatch.data.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import java.lang.ref.WeakReference

internal class GoogleAuthProxyActivity : Activity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val authIntent: Intent = this.intent.getParcelableExtra(AUTH_INTENT)
		this.startActivityForResult(authIntent, AUTH_REQUEST)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == AUTH_REQUEST && resultCode == RESULT_OK) {
			data?.let {
				listenerRef?.get()?.onSuccessfulActivityRequest(requestCode, data)
			} ?: listenerRef?.get()?.onFailedActivityRequest(requestCode)
			listenerRef = null
			finish()
		} else if (resultCode == RESULT_CANCELED) {
			listenerRef?.get()?.onFailedActivityRequest(requestCode)
			listenerRef = null
			finish()
		}
	}

	interface GoogleAuthenticationProxyActivityListener {
		fun onSuccessfulActivityRequest(requestCode: Int, data: Intent?)
		fun onFailedActivityRequest(requestCode: Int)
	}

	companion object {
		private const val AUTH_REQUEST = 100
		private const val AUTH_INTENT = "br.com.yves.groupmatch.authIntent"

		private var listenerRef: WeakReference<GoogleAuthenticationProxyActivityListener>? = null

		fun newIntent(context: Context, authIntent: Intent): Intent {
			return Intent(context, GoogleAuthProxyActivity::class.java).apply {
				putExtra(AUTH_INTENT, authIntent)
			}
		}

		fun addListener(listener: GoogleAuthenticationProxyActivityListener) {
			listenerRef = WeakReference(listener)
		}
	}
}