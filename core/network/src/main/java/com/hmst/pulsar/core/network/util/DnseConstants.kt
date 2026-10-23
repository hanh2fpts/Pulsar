package com.hmst.pulsar.core.network.util


internal object DnseConstants {
    /* kieu ket noi */
    const val ENCODING: String = "msgpack"

    /* URL websocket */
    const val DNSE_WEBSOCKET_URL: String = "wss://ws-openapi.dnse.com.vn/v1/stream?encoding=$ENCODING"

    /* GIẢ THUYẾT: KL của DNSE tính theo đơn vị 10 cp, cần đối chiếu */
    const val SHARES_PER_QTY_UNIT = 10
}