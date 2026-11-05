package com.onze.app.data

private val REGEX_EMAIL = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")

const val TAMANHO_MINIMO_SENHA = 6

fun emailValido(email: String): Boolean = REGEX_EMAIL.matches(email.trim())
