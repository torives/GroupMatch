package br.com.yves.groupmatch.presentation

import android.content.Context
import androidx.fragment.app.Fragment

fun Fragment.runOnUiThread(closure: (Context) -> Unit) {
	activity?.let {
		it.runOnUiThread {
			closure(it)
		}
	}
}