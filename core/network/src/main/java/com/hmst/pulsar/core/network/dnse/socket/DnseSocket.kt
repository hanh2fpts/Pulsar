package com.hmst.pulsar.core.network.dnse.socket

import android.util.Log
import com.hmst.pulsar.core.network.dnse.DnseConstants.DNSE_WEBSOCKET_URL
import com.hmst.pulsar.core.network.msgpack.MsgPack
import com.hmst.pulsar.core.network.util.kLog
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

class DnseSocket @Inject constructor(private val client: OkHttpClient) {
    private var ws: WebSocket? = null

    fun connect(onFrame: (Map<String, Any?>) -> Unit) {
        val request = Request.Builder().url(DNSE_WEBSOCKET_URL).build()
        ws = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                kLog(tag = "DNSE", message = "Đã mở kết nối, HTTP ${response.code}")
            }

            // server gửi BINARY frame
            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                @Suppress("UNCHECKED_CAST")
                val msg = MsgPack.decode(bytes.toByteArray()) as? Map<String, Any?>
                if (msg == null) {
                    kLog(tag = "DNSE", message = "Frame không giải mã được, ${bytes.size} bytes", type = Log.ERROR)
                    return
                }
                onFrame(msg)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                kLog(tag = "DNSE", message = "Nhận TEXT frame (không mong đợi): $text")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                kLog(tag = "DNSE", message = "Server đóng: code=$code reason=$reason")
                webSocket.close(code, reason)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                kLog(tag = "DNSE", message = "Lỗi kết nối: $code (HTTP ${reason})", type = Log.ERROR)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                kLog(tag = "DNSE", message = "Lỗi kết nối: ${t.message} (HTTP ${response?.code}) $t", type = Log.ERROR)
            }

        })
    }

    fun close() {
        ws?.close(1000, "bye"); ws = null
    }

    fun send(frame: ByteArray): Boolean =
        ws?.send(ByteString.of(*frame)) ?: false
}