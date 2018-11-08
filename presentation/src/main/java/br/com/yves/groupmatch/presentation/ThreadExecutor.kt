package br.com.yves.groupmatch.presentation

import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun runOnBackground(f: () -> Unit) {
	IO_EXECUTOR.execute(f)
}
