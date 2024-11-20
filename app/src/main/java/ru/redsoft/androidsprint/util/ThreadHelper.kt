package ru.redsoft.androidsprint.util

import java.util.concurrent.Executors

object ThreadHelper {
    val threadPool = Executors.newFixedThreadPool(10)
}