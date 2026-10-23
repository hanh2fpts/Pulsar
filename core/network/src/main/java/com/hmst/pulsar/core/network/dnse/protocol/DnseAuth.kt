package com.hmst.pulsar.core.network.dnse.protocol

import com.hmst.pulsar.core.network.msgpack.MsgPack
import java.util.concurrent.atomic.AtomicLong
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object DnseAuth {
    private val lastNonce = AtomicLong(0)

    fun frame(apiKey: String, apiSecret: String): ByteArray {
        val timestamp = System.currentTimeMillis() / 1000
        val nonce = lastNonce.updateAndGet { maxOf(it + 1, System.currentTimeMillis() * 1000) }

        val message = "$apiKey:$timestamp:$nonce"
        val mac = Mac.getInstance("HmacSHA256").apply {
            init(SecretKeySpec(apiSecret.toByteArray(Charsets.UTF_8), "HmacSHA256"))
        }
        val signature = mac.doFinal(message.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }

        return MsgPack.encode(
            mapOf(
                "action" to "auth",
                "api_key" to apiKey,
                "signature" to signature,
                "timestamp" to timestamp,
                "nonce" to nonce.toString(),
            )
        )
    }
}