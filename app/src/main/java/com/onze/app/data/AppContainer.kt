package com.onze.app.data

import android.content.Context
import com.onze.app.network.RetrofitClient

/** Cria e guarda as dependências do app (banco, sessão e repositórios). */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    private val banco: AppDatabase by lazy { AppDatabase.obter(appContext) }
    private val sessao: SessaoStore by lazy { SessaoStore(appContext) }

    val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(banco.usuarioDao(), sessao)
    }

    val jogadorRepository: JogadorRepository by lazy {
        JogadorRepository(RetrofitClient.api, banco.favoritoDao(), sessao)
    }
}
