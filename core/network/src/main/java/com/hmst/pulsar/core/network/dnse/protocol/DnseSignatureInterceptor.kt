package com.hmst.pulsar.core.network.dnse.protocol

import android.os.Build
import androidx.annotation.RequiresApi
import com.hmst.pulsar.core.network.util.DnseUtil.DNSE_API_VERSION
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.Locale
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal class DnseSignatureInterceptor(
    private val apiKey: String,
    private val apiSecret: String,
) : Interceptor {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val date = DATE_FORMAT.format(ZonedDateTime.now(ZoneOffset.UTC)) // "Sat, 24 Oct 2026 01:00:00 +0000"
        val nonce = UUID.randomUUID().toString().replace("-", "")

        // path KHÔNG kèm query string
        val message =
            "(request-target): ${request.method.lowercase()} ${request.url.encodedPath}\n" +
                    "date: $date\n" +
                    "nonce: $nonce"
        val mac = Mac.getInstance("HmacSHA256").apply {
            init(
                SecretKeySpec(
                    apiSecret.toByteArray(Charsets.UTF_8),
                    "HmacSHA256"
                )
            )
        }
        val signature = URLEncoder.encode(
            Base64.getEncoder().encodeToString(mac.doFinal(message.toByteArray(Charsets.UTF_8))),
            "UTF-8",
        )

        return chain.proceed(
            request.newBuilder()
                .header("Date", date)
                .header("x-api-key", apiKey)
                .header("version", DNSE_API_VERSION)
                .header(
                    "X-Signature",
                    "Signature keyId=\"$apiKey\",algorithm=\"hmac-sha256\"," +
                            "headers=\"(request-target) date\",signature=\"$signature\",nonce=\"$nonce\"",
                )
                .build()
        )
    }

    private companion object {
        // không dùng RFC_1123_DATE_TIME vì nó in UTC thành "GMT", SDK DNSE dùng "+0000"
        @RequiresApi(Build.VERSION_CODES.O)
        val DATE_FORMAT: DateTimeFormatter =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
    }
}
