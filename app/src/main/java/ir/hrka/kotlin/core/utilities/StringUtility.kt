package ir.hrka.kotlin.core.utilities

import android.util.Base64

fun String.decodeBase64(): String {
    val decodedBytes = Base64.decode(this, Base64.DEFAULT)
    return String(decodedBytes)
}