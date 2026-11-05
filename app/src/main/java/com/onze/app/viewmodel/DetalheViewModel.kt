package com.onze.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onze.app.data.Jogador
import com.onze.app.data.JogadorRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface DetalheUiState {
    data object Carregando : DetalheUiState
    data class Sucesso(val jogador: Jogador) : DetalheUiState
    data object NaoEncontrado : DetalheUiState
    data class Erro(val semConexao: Boolean) : DetalheUiState
}

class DetalheViewModel(
    private val repositorio: JogadorRepository,
    private val jogadorId: String
) : ViewModel() {

    var uiState: DetalheUiState by mutableStateOf(DetalheUiState.Carregando)
        private set

    val favorito: StateFlow<Boolean> = repositorio.favoritos
        .map { lista -> lista.any { it.id == jogadorId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            uiState = DetalheUiState.Carregando
            uiState = try {
                val jogador = repositorio.detalhar(jogadorId)
                if (jogador == null) DetalheUiState.NaoEncontrado else DetalheUiState.Sucesso(jogador)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                DetalheUiState.Erro(semConexao = true)
            } catch (e: Exception) {
                DetalheUiState.Erro(semConexao = false)
            }
        }
    }

    fun alternarFavorito() {
        val jogador = (uiState as? DetalheUiState.Sucesso)?.jogador ?: return
        viewModelScope.launch { repositorio.alternarFavorito(jogador) }
    }
}
