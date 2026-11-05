package com.onze.app.userinterface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onze.app.userinterface.theme.OnzeCores
import com.onze.app.viewmodel.ExplorarUiState
import com.onze.app.viewmodel.ExplorarViewModel
import com.onze.app.viewmodel.Fabricas

@Composable
fun ExplorarScreen(
    aoAbrirDetalhe: (String) -> Unit,
    modifier: Modifier = Modifier,
    vm: ExplorarViewModel = viewModel(factory = Fabricas.explorar)
) {
    val favoritos by vm.favoritosIds.collectAsStateWithLifecycle()
    val teclado = LocalSoftwareKeyboardController.current
    val buscar = {
        teclado?.hide()
        vm.buscar()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        OnzeLogo()
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Buscar jogadores",
            color = OnzeCores.Texto,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Encontre seu próximo favorito pelo nome.",
            color = OnzeCores.TextoSecundario,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))

        CampoOnze(
            rotulo = "Nome do jogador",
            valor = vm.termo,
            aoMudar = vm::aoMudarTermo,
            dica = "Ex.: Danny Welbeck",
            iconeInicial = Icons.Outlined.Search,
            limpavel = true,
            acaoIme = ImeAction.Search,
            aoAcaoIme = buscar
        )
        Spacer(Modifier.height(12.dp))
        BotaoOnze(texto = "Buscar", aoClicar = buscar, habilitado = vm.termo.isNotBlank())
        Spacer(Modifier.height(20.dp))

        when (val estado = vm.uiState) {
            is ExplorarUiState.Inicial -> {
                Text(
                    text = "Digite o nome de um jogador para começar.",
                    color = OnzeCores.TextoTerciario,
                    fontSize = 13.sp
                )
            }

            is ExplorarUiState.Carregando -> {
                Text("Buscando jogadores...", color = OnzeCores.TextoSecundario, fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                CartaoEsqueleto()
            }

            is ExplorarUiState.Erro -> {
                if (estado.semConexao) {
                    EstadoTela(
                        icone = Icons.Outlined.WifiOff,
                        titulo = "Sem conexão",
                        mensagem = "Não foi possível buscar os jogadores. Verifique sua internet e tente novamente.",
                        rotuloAcao = "Tentar novamente",
                        aoAcao = buscar
                    )
                } else {
                    EstadoTela(
                        icone = Icons.Outlined.ErrorOutline,
                        titulo = "Algo deu errado",
                        mensagem = "Tivemos um problema ao buscar os jogadores. Tente novamente em instantes.",
                        rotuloAcao = "Tentar novamente",
                        aoAcao = buscar
                    )
                }
            }

            is ExplorarUiState.SemResultados -> {
                EstadoTela(
                    icone = Icons.Outlined.SearchOff,
                    titulo = "Nenhum jogador encontrado",
                    mensagem = "Não encontramos resultados para \"${estado.termo}\". Confira o nome e faça uma nova busca."
                )
            }

            is ExplorarUiState.Sucesso -> {
                val total = estado.jogadores.size
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Resultados", color = OnzeCores.Texto, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (total == 1) "1 jogador encontrado" else "$total jogadores encontrados",
                        color = OnzeCores.TextoSecundario,
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(estado.jogadores, key = { it.id }) { jogador ->
                        val favorito = jogador.id in favoritos
                        CartaoJogador(
                            jogador = jogador,
                            aoClicar = { aoAbrirDetalhe(jogador.id) }
                        ) {
                            IconButton(onClick = { vm.alternarFavorito(jogador) }) {
                                Icon(
                                    imageVector = if (favorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = if (favorito) "Remover dos favoritos" else "Favoritar",
                                    tint = if (favorito) OnzeCores.Verde else OnzeCores.TextoSecundario
                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Toque no jogador para ver os detalhes.",
                            color = OnzeCores.TextoTerciario,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
