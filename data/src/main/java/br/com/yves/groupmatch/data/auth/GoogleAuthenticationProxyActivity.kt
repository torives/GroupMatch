package br.com.yves.groupmatch.data.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

internal class GoogleAuthenticationProxyActivity : Activity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val authIntent: Intent = this.intent.getParcelableExtra(AUTH_INTENT)
		this.startActivityForResult(authIntent, AUTH_REQUEST)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == AUTH_REQUEST && data != null) {
			GroupMatchAuth.instance.onGoogleAuthenticationResult(data)
			finish()
		}
	}

	companion object {
		private const val AUTH_REQUEST = 100
		private const val AUTH_INTENT = "br.com.yves.groupmatch.authIntent"

		fun newIntent(context: Context, authIntent: Intent): Intent {
			return Intent(context, GoogleAuthenticationProxyActivity::class.java).apply {
				putExtra(AUTH_INTENT, authIntent)
			}
		}
	}
}