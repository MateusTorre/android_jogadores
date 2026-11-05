package com.onze.app.userinterface

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.onze.app.userinterface.theme.OnzeCores
import com.onze.app.viewmodel.Fabricas
import com.onze.app.viewmodel.SessaoViewModel

/** As 3 abas da barra inferior. */
private enum class Aba(
    val rotulo: String,
    val rota: Any,
    val iconeSelecionado: ImageVector,
    val icone: ImageVector
) {
    Explorar("Explorar", RotaExplorar, Icons.Filled.Search, Icons.Outlined.Search),
    Favoritos("Favoritos", RotaFavoritos, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    Perfil("Perfil", RotaPerfil, Icons.Filled.Person, Icons.Outlined.Person)
}

private fun abaDe(destino: NavDestination?): Aba? = when {
    destino == null -> null
    destino.hasRoute<RotaExplorar>() -> Aba.Explorar
    destino.hasRoute<RotaFavoritos>() -> Aba.Favoritos
    destino.hasRoute<RotaPerfil>() -> Aba.Perfil
    else -> null
}

@Composable
fun OnzeApp(sessao: SessaoViewModel = viewModel(factory = Fabricas.sessao)) {
    val navController = rememberNavController()

    // Quem já estava logado abre direto nas abas; quem não estava, no login.
    val jaLogado = remember { sessao.logadoAgora }

    val entradaAtual by navController.currentBackStackEntryAsState()
    val destino = entradaAtual?.destination
    val abaAtual: Aba? = when {
        destino == null -> null
        destino.hasRoute<RotaDetalhe>() ->
            // o detalhe mantém marcada a aba de onde o jogador foi aberto
            abaDe(navController.previousBackStackEntry?.destination) ?: Aba.Explorar
        else -> abaDe(destino)
    }

    // Só quem entrou vê o app: login e cadastro ficam sem barra inferior.
    val mostrarBarra = abaAtual != null

    Scaffold(
        containerColor = OnzeCores.Fundo,
        bottomBar = {
            if (mostrarBarra) {
                BarraInferior(abaAtual = abaAtual) { aba ->
                    navController.navigate(aba.rota) {
                        popUpTo<RotaExplorar> { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { padding ->
        val grafo: NavGraphBuilder.() -> Unit = {
            montarGrafo(navController)
        }
        if (jaLogado) {
            NavHost(
                navController = navController,
                startDestination = RotaExplorar,
                modifier = Modifier.padding(padding),
                builder = grafo
            )
        } else {
            NavHost(
                navController = navController,
                startDestination = RotaLogin,
                modifier = Modifier.padding(padding),
                builder = grafo
            )
        }
    }
}

private fun NavGraphBuilder.montarGrafo(navController: NavHostController) {

    // Depois de entrar (login ou cadastro), limpa o histórico: voltar não leva ao login.
    val irParaApp: () -> Unit = {
        navController.navigate(RotaExplorar) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    composable<RotaLogin> {
        LoginScreen(
            aoEntrar = irParaApp,
            aoIrParaCadastro = { navController.navigate(RotaCadastro) }
        )
    }

    composable<RotaCadastro> {
        CadastroScreen(
            aoVoltar = { navController.popBackStack() },
            aoCadastrar = irParaApp
        )
    }

    composable<RotaExplorar> {
        ExplorarScreen(
            aoAbrirDetalhe = { id -> navController.navigate(RotaDetalhe(jogadorId = id)) }
        )
    }

    composable<RotaFavoritos> {
        FavoritosScreen(
            aoAbrirDetalhe = { id -> navController.navigate(RotaDetalhe(jogadorId = id)) }
        )
    }

    composable<RotaPerfil> {
        PerfilScreen(
            aoSair = {
                navController.navigate(RotaLogin) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        )
    }

    composable<RotaDetalhe> { entrada ->
        // rota type-safe: o id do jogador escolhido chega como parâmetro tipado
        val rota = entrada.toRoute<RotaDetalhe>()
        DetalheScreen(
            jogadorId = rota.jogadorId,
            aoVoltar = { navController.popBackStack() }
        )
    }
}

@Composable
private fun BarraInferior(abaAtual: Aba?, aoSelecionar: (Aba) -> Unit) {
    NavigationBar(containerColor = OnzeCores.Superficie) {
        Aba.entries.forEach { aba ->
            val selecionada = aba == abaAtual
            NavigationBarItem(
                selected = selecionada,
                onClick = { aoSelecionar(aba) },
                icon = {
                    Icon(
                        imageVector = if (selecionada) aba.iconeSelecionado else aba.icone,
                        contentDescription = aba.rotulo
                    )
                },
                label = { Text(aba.rotulo, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OnzeCores.Verde,
                    selectedTextColor = OnzeCores.Verde,
                    indicatorColor = OnzeCores.VerdeEscuro,
                    unselectedIconColor = OnzeCores.TextoSecundario,
                    unselectedTextColor = OnzeCores.TextoSecundario
                )
            )
        }
    }
}
