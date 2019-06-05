package br.com.yves.groupmatch.domain

open class GroupMatchError(
		val code: Int,
		override val message: String,
		val exception: Exception? = null
): Error(message, exception)