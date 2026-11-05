package com.onze.app.data

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

enum class CampoAuth { NOME, EMAIL, SENHA, CONFIRMACAO }

sealed interface ResultadoAuth {
    data object Sucesso : ResultadoAuth

    /** [erros] são mensagens por campo (cadastro); [geral] é uma mensagem única (login). */
    data class Falha(
        val erros: Map<CampoAuth, String> = emptyMap(),
        val geral: String? = null
    ) : ResultadoAuth
}

class UsuarioRepository(
    private val dao: UsuarioDao,
    private val sessao: SessaoStore
) {

    /** Id do usuário logado (null = ninguém logado). */
    val usuarioId: StateFlow<Long?> get() = sessao.usuarioId

    @OptIn(ExperimentalCoroutinesApi::class)
    val usuario: Flow<Usuario?> = sessao.usuarioId.flatMapLatest { id ->
        if (id == null) flowOf(null) else dao.observarPorId(id)
    }

    suspend fun cadastrar(
        nome: String,
        email: String,
        senha: String,
        confirmacao: String
    ): ResultadoAuth {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase()

        val erros = mutableMapOf<CampoAuth, String>()
        if (nomeLimpo.length < 2) erros[CampoAuth.NOME] = "Informe seu nome."
        if (!emailValido(emailLimpo)) erros[CampoAuth.EMAIL] = "Digite um e-mail válido."
        if (senha.length < TAMANHO_MINIMO_SENHA) {
            erros[CampoAuth.SENHA] = "A senha precisa ter $TAMANHO_MINIMO_SENHA ou mais caracteres."
        }
        if (confirmacao != senha) erros[CampoAuth.CONFIRMACAO] = "As senhas não são iguais."
        if (erros.isNotEmpty()) return ResultadoAuth.Falha(erros = erros)

        val jaExiste = ResultadoAuth.Falha(mapOf(CampoAuth.EMAIL to "Este e-mail já está cadastrado."))
        if (dao.buscarPorEmail(emailLimpo) != null) return jaExiste

        val id = try {
            dao.inserir(Usuario(nome = nomeLimpo, email = emailLimpo, senhaHash = senha.sha256()))
        } catch (e: SQLiteConstraintException) {
            return jaExiste
        }

        sessao.entrar(id)
        return ResultadoAuth.Sucesso
    }

    suspend fun entrar(email: String, senha: String): ResultadoAuth {
        if (email.isBlank() || senha.isEmpty()) {
            return ResultadoAuth.Falha(geral = "Informe seu e-mail e sua senha.")
        }
        val usuario = dao.buscarPorEmail(email.trim().lowercase())
        // mesma mensagem para e-mail inexistente e senha errada: não revela qual campo errou
        if (usuario == null || usuario.senhaHash != senha.sha256()) {
            return ResultadoAuth.Falha(geral = "E-mail ou senha incorretos.")
        }
        sessao.entrar(usuario.id)
        return ResultadoAuth.Sucesso
    }

    fun sair() = sessao.sair()
}
