package com.onze.app.userinterface.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object OnzeCores {
    val Fundo = Color(0xFF131613)
    val Superficie = Color(0xFF1B1F1B)
    val SuperficieAlta = Color(0xFF242924)
    val Borda = Color(0xFF2F362F)
    val Verde = Color(0xFF8FE388)
    val VerdeEscuro = Color(0xFF24402A)
    val SobreVerde = Color(0xFF0B2A0D)
    val Texto = Color(0xFFF1F5F1)
    val TextoSecundario = Color(0xFF9AA39A)
    val TextoTerciario = Color(0xFF6B746B)
    val Erro = Color(0xFFFF7B7B)
}

private val EsquemaEscuro = darkColorScheme(
    primary = OnzeCores.Verde,
    onPrimary = OnzeCores.SobreVerde,
    background = OnzeCores.Fundo,
    onBackground = OnzeCores.Texto,
    surface = OnzeCores.Superficie,
    onSurface = OnzeCores.Texto,
    surfaceVariant = OnzeCores.SuperficieAlta,
    onSurfaceVariant = OnzeCores.TextoSecundario,
    outline = OnzeCores.Borda,
    error = OnzeCores.Erro
)

@Composable
fun OnzeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaEscuro, content = content)
}
