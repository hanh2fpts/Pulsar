package com.hmst.pulsar.core.network.util


internal object DnseUtil {
    /* kieu ket noi */
    const val ENCODING: String = "msgpack"

    /* URL websocket */
    const val DNSE_WEBSOCKET_URL: String =
        "wss://ws-openapi.dnse.com.vn/v1/stream?encoding=$ENCODING"

    /*URL REST */
    const val DNSE_REST_URL: String = "https://openapi.dnse.com.vn/"

    /*header version*/
    const val DNSE_API_VERSION: String = "2026-07-23"
}