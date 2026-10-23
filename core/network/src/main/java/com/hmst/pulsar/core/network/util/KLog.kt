package com.hmst.pulsar.core.network.util

import android.util.Log

fun kLog(message: String = "Log message", tag: String = "PULSAR_LOG", type: Int = Log.DEBUG) {
    when (type) {
        Log.DEBUG -> Log.d(tag, message)
        Log.ERROR -> Log.e(tag, message)
        Log.INFO -> Log.i(tag, message)
        Log.VERBOSE -> Log.v(tag, message)
        Log.WARN -> Log.w(tag, message)
    }
}

