package com.onze.app.network

import com.onze.app.data.BuscaResponse
import com.onze.app.data.DetalheResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TheSportsDbApi {

    @GET("searchplayers.php")
    suspend fun buscarJogadores(@Query("p") nome: String): BuscaResponse

    @GET("lookupplayer.php")
    suspend fun detalharJogador(@Query("id") id: String): DetalheResponse
}
