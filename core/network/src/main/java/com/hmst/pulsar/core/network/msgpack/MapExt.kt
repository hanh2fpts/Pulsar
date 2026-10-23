package com.hmst.pulsar.core.network.msgpack

fun Map<String, Any?>.strExt(k: String): String? = this[k] as? String
fun Map<String, Any?>.doubleExt(k: String): Double? = (this[k] as? Number)?.toDouble()
fun Map<String, Any?>.longExt(k: String): Long? = (this[k] as? Number)?.toLong()

/** time = {Seconds, Nanos} (epoch UTC) -> epoch milli */
fun Map<String, Any?>.timeMs(k: String): Long? {
    val t = this[k] as? Map<*, *> ?: return null
    val s = (t["Seconds"] as? Number)?.toLong() ?: return null
    val n = (t["Nanos"] as? Number)?.toLong() ?: 0L
    return s * 1000 + n / 1_000_000
}