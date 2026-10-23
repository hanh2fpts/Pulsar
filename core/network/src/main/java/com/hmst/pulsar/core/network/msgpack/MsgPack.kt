package com.hmst.pulsar.core.network.msgpack

import org.msgpack.core.MessagePack
import org.msgpack.core.MessagePacker
import org.msgpack.value.Value
import org.msgpack.value.ValueType

object MsgPack {
    fun decode(bytes: ByteArray): Any? =
        MessagePack.newDefaultUnpacker(bytes).use { convert(it.unpackValue()) }

    private fun convert(v: Value): Any? = when (v.valueType) {
        ValueType.NIL -> null
        ValueType.BOOLEAN -> v.asBooleanValue().boolean
        ValueType.INTEGER -> v.asIntegerValue().toLong()
        ValueType.FLOAT -> v.asFloatValue().toDouble()
        ValueType.STRING -> v.asStringValue().asString()
        ValueType.BINARY -> v.asBinaryValue().asByteArray()
        ValueType.ARRAY -> v.asArrayValue().list().map(::convert)
        ValueType.MAP -> v.asMapValue().map().entries
            .associate { (k, x) -> convert(k).toString() to convert(x) }

        ValueType.EXTENSION -> null
    }

    fun encode(v: Any?): ByteArray =
        MessagePack.newDefaultBufferPacker().use { p -> pack(p, v); p.toByteArray() }

    private fun pack(p: MessagePacker, v: Any?) {
        when (v) {
            null -> p.packNil()
            is Boolean -> p.packBoolean(v)
            is Int -> p.packInt(v)
            is Long -> p.packLong(v)
            is Double -> p.packDouble(v)
            is String -> p.packString(v)
            is Map<*, *> -> {
                p.packMapHeader(v.size)
                v.forEach { (k, x) -> pack(p, k); pack(p, x) }
            }

            is Collection<*> -> {
                p.packArrayHeader(v.size)
                v.forEach { pack(p, it) }
            }

            else -> error("Unsupported type: ${v::class}")
        }
    }
}