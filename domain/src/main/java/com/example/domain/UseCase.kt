package com.example.domain

abstract class UseCase<out T> {
    abstract fun execute(): T
}