package com.onze.app.userinterface

import kotlinx.serialization.Serializable

/** Rotas type-safe do Navigation Compose. Detalhe leva o id do jogador escolhido. */
@Serializable object RotaLogin
@Serializable object RotaCadastro
@Serializable object RotaExplorar
@Serializable object RotaFavoritos
@Serializable object RotaPerfil
@Serializable data class RotaDetalhe(val jogadorId: String)
