package com.onze.app.data

import com.onze.app.network.TheSportsDbApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class JogadorRepository(
    private val api: TheSportsDbApi,
    private val favoritoDao: FavoritoDao,
    private val sessao: SessaoStore
) {

    // jogadores vistos nas buscas: abrir o detalhe não precisa de nova chamada
    private val cache = mutableMapOf<String, Jogador>()

    /** Favoritos do usuário logado; troca sozinho quando outra conta entra. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val favoritos: Flow<List<Jogador>> = sessao.usuarioId.flatMapLatest { usuarioId ->
        if (usuarioId == null) {
            flowOf(emptyList())
        } else {
            favoritoDao.observar(usuarioId).map { lista -> lista.map { it.toJogador() } }
        }
    }

    suspend fun buscar(nome: String): List<Jogador> {
        val resposta = api.buscarJogadores(nome.trim())
        val lista = resposta.jogadores.orEmpty()
            .filter { it.esporte == null || it.esporte.equals("Soccer", ignoreCase = true) }
            .mapNotNull { it.toJogador() }
        lista.forEach { cache[it.id] = it }
        return lista
    }

    /** Cache da busca -> favorito salvo (funciona offline) -> API. Lança exceção se a rede falhar. */
    suspend fun detalhar(id: String): Jogador? {
        cache[id]?.let { return it }

        sessao.usuarioId.value?.let { usuarioId ->
            favoritoDao.buscar(usuarioId, id)?.let { return it.toJogador() }
        }

        val jogador = api.detalharJogador(id).jogadores?.firstOrNull()?.toJogador()
        if (jogador != null) cache[id] = jogador
        return jogador
    }

    suspend fun alternarFavorito(jogador: Jogador) {
        val usuarioId = sessao.usuarioId.value ?: return
        if (favoritoDao.buscar(usuarioId, jogador.id) != null) {
            favoritoDao.remover(usuarioId, jogador.id)
        } else {
            favoritoDao.inserir(jogador.toFavorito(usuarioId))
        }
    }

    suspend fun remover(jogadorId: String) {
        val usuarioId = sessao.usuarioId.value ?: return
        favoritoDao.remover(usuarioId, jogadorId)
    }
}
