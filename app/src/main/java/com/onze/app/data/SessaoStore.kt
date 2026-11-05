package com.onze.app.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Guarda o id do usuário logado em SharedPreferences, para a sessão
 * sobreviver ao fechar e reabrir o app.
 */
class SessaoStore(context: Context) {

    private val prefs = context.getSharedPreferences("onze_sessao", Context.MODE_PRIVATE)

    private val _usuarioId = MutableStateFlow(lerId())
    val usuarioId: StateFlow<Long?> = _usuarioId.asStateFlow()

    fun entrar(id: Long) {
        prefs.edit().putLong(CHAVE_USUARIO, id).apply()
        _usuarioId.value = id
    }

    fun sair() {
        prefs.edit().remove(CHAVE_USUARIO).apply()
        _usuarioId.value = null
    }

    private fun lerId(): Long? =
        if (prefs.contains(CHAVE_USUARIO)) prefs.getLong(CHAVE_USUARIO, -1L) else null

    private companion object {
        const val CHAVE_USUARIO = "usuario_id"
    }
}
