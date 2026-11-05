package com.onze.app.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Resposta de searchplayers.php (o campo vem null quando nada é encontrado). */
@Serializable
data class BuscaResponse(
    @SerialName("player") val jogadores: List<JogadorDto>? = null
)

/** Resposta de lookupplayer.php. */
@Serializable
data class DetalheResponse(
    @SerialName("players") val jogadores: List<JogadorDto>? = null
)

@Serializable
data class JogadorDto(
    @SerialName("idPlayer") val id: String? = null,
    @SerialName("strPlayer") val nome: String? = null,
    @SerialName("strPlayerAlternate") val nomeAlternativo: String? = null,
    @SerialName("strSport") val esporte: String? = null,
    @SerialName("strNationality") val nacionalidade: String? = null,
    @SerialName("strPosition") val posicao: String? = null,
    @SerialName("strTeam") val clube: String? = null,
    @SerialName("strNumber") val numero: String? = null,
    @SerialName("dateBorn") val nascimento: String? = null,
    @SerialName("strThumb") val miniatura: String? = null,
    @SerialName("strCutout") val recorte: String? = null
)

/** Modelo usado pelo app inteiro (API e favoritos). */
data class Jogador(
    val id: String,
    val nome: String,
    val nomeCompleto: String?,
    val nacionalidade: String?,
    val posicao: String?,
    val clube: String?,
    val numero: String?,
    val nascimento: String?,
    val foto: String?
)

fun JogadorDto.toJogador(): Jogador? {
    val idOk = id?.takeIf { it.isNotBlank() } ?: return null
    val nomeOk = nome?.takeIf { it.isNotBlank() } ?: return null
    return Jogador(
        id = idOk,
        nome = nomeOk,
        nomeCompleto = nomeAlternativo?.takeIf { it.isNotBlank() && !it.equals(nomeOk, ignoreCase = true) },
        nacionalidade = nacionalidade?.takeIf { it.isNotBlank() },
        posicao = posicao?.takeIf { it.isNotBlank() },
        // a API usa "_Retired Soccer", "_No Team" etc. para quem não tem clube
        clube = clube?.takeIf { it.isNotBlank() && !it.startsWith("_") },
        numero = numero?.takeIf { it.isNotBlank() },
        nascimento = nascimento?.takeIf { it.isNotBlank() },
        foto = (miniatura ?: recorte)?.takeIf { it.isNotBlank() }
    )
}
