package com.hmst.pulsar.core.network.dnse.protocol

import com.hmst.pulsar.core.network.msgpack.MsgPack

object DnseSubscribe {
    /** channels: tên channel -> danh sách mã (viết hoa) */
    fun frame(channels: Map<String, List<String>>): ByteArray =
        MsgPack.encode(mapOf(
            "action" to "subscribe",
            "channels" to channels.map { (name, symbols) ->
                mapOf("name" to name, "symbols" to symbols.map { it.uppercase() })
            },
        ))
}