package com.onze.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onze.app.data.Jogador
import com.onze.app.data.JogadorRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface ExplorarUiState {
    data object Inicial : ExplorarUiState
    data object Carregando : ExplorarUiState
    data class Sucesso(val jogadores: List<Jogador>) : ExplorarUiState
    data class SemResultados(val termo: String) : ExplorarUiState
    data class Erro(val semConexao: Boolean) : ExplorarUiState
}

class ExplorarViewModel(private val repositorio: JogadorRepository) : ViewModel() {

    var termo by mutableStateOf("")
        private set

    var uiState: ExplorarUiState by mutableStateOf(ExplorarUiState.Inicial)
        private set

    /** Ids dos jogadores favoritos do usuário logado (para pintar o coração). */
    val favoritosIds: StateFlow<Set<String>> = repositorio.favoritos
        .map { lista -> lista.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    private var buscaJob: Job? = null

    fun aoMudarTermo(valor: String) {
        termo = valor
    }

    fun buscar() {
        val consulta = termo.trim()
        if (consulta.isBlank()) return

        buscaJob?.cancel()
        buscaJob = viewModelScope.launch {
            uiState = ExplorarUiState.Carregando
            uiState = try {
                val lista = repositorio.buscar(consulta)
                if (lista.isEmpty()) ExplorarUiState.SemResultados(consulta)
                else ExplorarUiState.Sucesso(lista)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                ExplorarUiState.Erro(semConexao = true)
            } catch (e: Exception) {
                ExplorarUiState.Erro(semConexao = false)
            }
        }
    }

    fun alternarFavorito(jogador: Jogador) {
        viewModelScope.launch { repositorio.alternarFavorito(jogador) }
    }
}
