package com.onze.app.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.onze.app.OnzeApplication
import com.onze.app.data.AppContainer

private fun CreationExtras.container(): AppContainer =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OnzeApplication).container

/** Fábricas que entregam os repositórios para cada ViewModel. */
object Fabricas {

    val sessao = viewModelFactory {
        initializer { SessaoViewModel(container().usuarioRepository) }
    }

    val login = viewModelFactory {
        initializer { LoginViewModel(container().usuarioRepository) }
    }

    val cadastro = viewModelFactory {
        initializer { CadastroViewModel(container().usuarioRepository) }
    }

    val explorar = viewModelFactory {
        initializer { ExplorarViewModel(container().jogadorRepository) }
    }

    val favoritos = viewModelFactory {
        initializer { FavoritosViewModel(container().jogadorRepository) }
    }

    /** O id do jogador chega da rota type-safe (lida na tela) e é passado aqui. */
    fun detalhe(jogadorId: String) = viewModelFactory {
        initializer { DetalheViewModel(container().jogadorRepository, jogadorId) }
    }
}
