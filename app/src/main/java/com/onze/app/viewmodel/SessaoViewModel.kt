package com.onze.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onze.app.data.Usuario
import com.onze.app.data.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SessaoViewModel(private val repositorio: UsuarioRepository) : ViewModel() {

    /** Valor no momento de abrir o app: decide se começa no login ou nas abas. */
    val logadoAgora: Boolean get() = repositorio.usuarioId.value != null

    val usuario: StateFlow<Usuario?> = repositorio.usuario
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun sair() = repositorio.sair()
}
