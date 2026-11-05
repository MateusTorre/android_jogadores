package com.onze.app.userinterface

/** Textos da API (em inglês) apresentados em português, como no protótipo. */

private val POSICOES = mapOf(
    "goalkeeper" to "Goleiro",
    "defender" to "Defensor",
    "centre-back" to "Zagueiro",
    "center-back" to "Zagueiro",
    "left-back" to "Lateral-esquerdo",
    "right-back" to "Lateral-direito",
    "left wing-back" to "Ala-esquerdo",
    "right wing-back" to "Ala-direito",
    "midfielder" to "Meio-campista",
    "defensive midfield" to "Volante",
    "central midfield" to "Meio-campista",
    "attacking midfield" to "Meia-atacante",
    "left midfield" to "Meia-esquerda",
    "right midfield" to "Meia-direita",
    "left winger" to "Ponta-esquerda",
    "right winger" to "Ponta-direita",
    "second striker" to "Segundo atacante",
    "centre-forward" to "Centroavante",
    "center-forward" to "Centroavante",
    "striker" to "Atacante",
    "forward" to "Atacante",
    "manager" to "Técnico"
)

private val PAISES = mapOf(
    "england" to "Inglaterra",
    "scotland" to "Escócia",
    "wales" to "País de Gales",
    "northern ireland" to "Irlanda do Norte",
    "ireland" to "Irlanda",
    "brazil" to "Brasil",
    "argentina" to "Argentina",
    "uruguay" to "Uruguai",
    "chile" to "Chile",
    "colombia" to "Colômbia",
    "paraguay" to "Paraguai",
    "ecuador" to "Equador",
    "peru" to "Peru",
    "venezuela" to "Venezuela",
    "mexico" to "México",
    "united states" to "Estados Unidos",
    "canada" to "Canadá",
    "portugal" to "Portugal",
    "spain" to "Espanha",
    "france" to "França",
    "germany" to "Alemanha",
    "italy" to "Itália",
    "netherlands" to "Países Baixos",
    "belgium" to "Bélgica",
    "switzerland" to "Suíça",
    "austria" to "Áustria",
    "denmark" to "Dinamarca",
    "sweden" to "Suécia",
    "norway" to "Noruega",
    "poland" to "Polônia",
    "croatia" to "Croácia",
    "serbia" to "Sérvia",
    "turkey" to "Turquia",
    "greece" to "Grécia",
    "russia" to "Rússia",
    "ukraine" to "Ucrânia",
    "egypt" to "Egito",
    "morocco" to "Marrocos",
    "senegal" to "Senegal",
    "nigeria" to "Nigéria",
    "ghana" to "Gana",
    "ivory coast" to "Costa do Marfim",
    "cameroon" to "Camarões",
    "algeria" to "Argélia",
    "japan" to "Japão",
    "south korea" to "Coreia do Sul",
    "australia" to "Austrália"
)

fun traduzirPosicao(posicao: String?): String =
    posicao?.let { POSICOES[it.trim().lowercase()] ?: it } ?: "Posição não informada"

fun traduzirPais(pais: String?): String =
    pais?.let { PAISES[it.trim().lowercase()] ?: it } ?: "Não informado"

/** "1990-11-26" -> "26/11/1990". */
fun formatarData(data: String?): String {
    if (data.isNullOrBlank()) return "Não informada"
    val partes = data.trim().split("-")
    return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else data
}

/** "Marina Silva" -> "MS". */
fun siglaDe(nome: String): String =
    nome.trim().split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }
        .ifEmpty { "?" }
