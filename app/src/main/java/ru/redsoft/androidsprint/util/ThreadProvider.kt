package ru.redsoft.androidsprint.util

import java.util.concurrent.Executors

object ThreadProvider {
    val threadPool = Executors.newFixedThreadPool(10)
}