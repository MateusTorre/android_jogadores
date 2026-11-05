package com.onze.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onze.app.data.Jogador
import com.onze.app.data.JogadorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repositorio: JogadorRepository) : ViewModel() {

    /** null enquanto o Room ainda não respondeu (evita piscar o estado "vazio"). */
    val favoritos: StateFlow<List<Jogador>?> = repositorio.favoritos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun remover(jogador: Jogador) {
        viewModelScope.launch { repositorio.remover(jogador.id) }
    }
}
