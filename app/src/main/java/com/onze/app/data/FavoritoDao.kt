package com.onze.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId ORDER BY salvoEm DESC")
    fun observar(usuarioId: Long): Flow<List<Favorito>>

    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId AND jogadorId = :jogadorId LIMIT 1")
    suspend fun buscar(usuarioId: Long, jogadorId: String): Favorito?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(favorito: Favorito)

    @Query("DELETE FROM favoritos WHERE usuarioId = :usuarioId AND jogadorId = :jogadorId")
    suspend fun remover(usuarioId: Long, jogadorId: String)
}
