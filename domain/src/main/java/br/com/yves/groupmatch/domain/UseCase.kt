package br.com.yves.groupmatch.domain

abstract class UseCase<out T> {
	abstract fun execute(): T
}