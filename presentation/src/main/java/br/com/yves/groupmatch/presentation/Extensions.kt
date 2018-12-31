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

fun Byte.toPositiveInt() = toInt() and 0xFF

fun Int.toByteArray(): ByteArray {
	val mask = 0xFF
	val result = ByteArray(Int.SIZE_BITS /8)
	var number = unaryPlus()

val map = mapOf<String, String>()
	for((timeSlot, busyUsers) in map) {

	}

	for(i in 0 until result.size) {
		result[i] = number.and(mask).toByte()
		number = number.shr(8)
	}
	return result.reversedArray()
}

fun ByteArray.toInt(): Int {
	var result = 0
	for (i in 0..lastIndex) {
		val shiftedByte = get(i).toPositiveInt().shl((count()-1-i)*8)
		result = result.or(shiftedByte)
	}
	return result
}