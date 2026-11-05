package com.onze.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/** Um jogador salvo por um usuário. A chave é (usuário, jogador): cada conta tem a sua lista. */
@Entity(
    tableName = "favoritos",
    primaryKeys = ["usuarioId", "jogadorId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("usuarioId")]
)
data class Favorito(
    val usuarioId: Long,
    val jogadorId: String,
    val nome: String,
    val nomeCompleto: String?,
    val nacionalidade: String?,
    val posicao: String?,
    val clube: String?,
    val numero: String?,
    val nascimento: String?,
    val foto: String?,
    val salvoEm: Long = System.currentTimeMillis()
)

fun Favorito.toJogador() = Jogador(
    id = jogadorId,
    nome = nome,
    nomeCompleto = nomeCompleto,
    nacionalidade = nacionalidade,
    posicao = posicao,
    clube = clube,
    numero = numero,
    nascimento = nascimento,
    foto = foto
)

fun Jogador.toFavorito(usuarioId: Long) = Favorito(
    usuarioId = usuarioId,
    jogadorId = id,
    nome = nome,
    nomeCompleto = nomeCompleto,
    nacionalidade = nacionalidade,
    posicao = posicao,
    clube = clube,
    numero = numero,
    nascimento = nascimento,
    foto = foto
)
