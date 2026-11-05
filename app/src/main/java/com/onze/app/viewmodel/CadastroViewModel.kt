package com.onze.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onze.app.data.CampoAuth
import com.onze.app.data.ResultadoAuth
import com.onze.app.data.UsuarioRepository
import kotlinx.coroutines.launch

class CadastroViewModel(private val repositorio: UsuarioRepository) : ViewModel() {

    var nome by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var senha by mutableStateOf("")
        private set
    var confirmacao by mutableStateOf("")
        private set
    var erros by mutableStateOf<Map<CampoAuth, String>>(emptyMap())
        private set
    var carregando by mutableStateOf(false)
        private set

    fun aoMudarNome(valor: String) { nome = valor; limparErro(CampoAuth.NOME) }
    fun aoMudarEmail(valor: String) { email = valor; limparErro(CampoAuth.EMAIL) }
    fun aoMudarSenha(valor: String) { senha = valor; limparErro(CampoAuth.SENHA) }
    fun aoMudarConfirmacao(valor: String) { confirmacao = valor; limparErro(CampoAuth.CONFIRMACAO) }

    private fun limparErro(campo: CampoAuth) {
        if (campo in erros) erros = erros - campo
    }

    fun cadastrar(aoSucesso: () -> Unit) {
        if (carregando) return
        viewModelScope.launch {
            carregando = true
            when (val resultado = repositorio.cadastrar(nome, email, senha, confirmacao)) {
                is ResultadoAuth.Sucesso -> aoSucesso()
                is ResultadoAuth.Falha -> erros = resultado.erros
            }
            carregando = false
        }
    }
}
