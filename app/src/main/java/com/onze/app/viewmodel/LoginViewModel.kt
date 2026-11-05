package com.onze.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onze.app.data.ResultadoAuth
import com.onze.app.data.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repositorio: UsuarioRepository) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var senha by mutableStateOf("")
        private set
    var erro by mutableStateOf<String?>(null)
        private set
    var carregando by mutableStateOf(false)
        private set

    fun aoMudarEmail(valor: String) {
        email = valor
        erro = null
    }

    fun aoMudarSenha(valor: String) {
        senha = valor
        erro = null
    }

    fun entrar(aoSucesso: () -> Unit) {
        if (carregando) return
        viewModelScope.launch {
            carregando = true
            when (val resultado = repositorio.entrar(email, senha)) {
                is ResultadoAuth.Sucesso -> {
                    senha = ""
                    aoSucesso()
                }
                is ResultadoAuth.Falha -> erro = resultado.geral ?: "Não foi possível entrar."
            }
            carregando = false
        }
    }
}
