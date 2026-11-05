package com.onze.app.data

/** SHA-256 em hexadecimal. O banco guarda só isso, nunca a senha digitada. */
fun String.sha256(): String =
    java.security.MessageDigest.getInstance("SHA-256")
        .digest(toByteArray()).joinToString("") { "%02x".format(it) }
