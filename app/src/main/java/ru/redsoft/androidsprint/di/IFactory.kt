package ru.redsoft.androidsprint.di

interface IFactory<T> {
    fun create(): T
}

