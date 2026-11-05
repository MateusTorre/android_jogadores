package com.onze.app.userinterface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onze.app.data.Jogador
import com.onze.app.userinterface.theme.OnzeCores
import com.onze.app.viewmodel.DetalheUiState
import com.onze.app.viewmodel.DetalheViewModel
import com.onze.app.viewmodel.Fabricas

@Composable
fun DetalheScreen(
    jogadorId: String,
    aoVoltar: () -> Unit,
    modifier: Modifier = Modifier,
    vm: DetalheViewModel = viewModel(factory = Fabricas.detalhe(jogadorId))
) {
    val favorito by vm.favorito.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = aoVoltar, contentPadding = PaddingValues(0.dp)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = OnzeCores.TextoSecundario,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Voltar", color = OnzeCores.TextoSecundario)
            }
            Text("Detalhes do jogador", color = OnzeCores.TextoSecundario, fontSize = 13.sp)
        }

        when (val estado = vm.uiState) {
            is DetalheUiState.Carregando -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OnzeCores.Verde)
                }
            }

            is DetalheUiState.Erro -> {
                EstadoTela(
                    icone = if (estado.semConexao) Icons.Outlined.WifiOff else Icons.Outlined.ErrorOutline,
                    titulo = if (estado.semConexao) "Sem conexão" else "Algo deu errado",
                    mensagem = if (estado.semConexao) {
                        "Não foi possível carregar o jogador. Verifique sua internet e tente novamente."
                    } else {
                        "Tivemos um problema ao carregar o jogador. Tente novamente em instantes."
                    },
                    rotuloAcao = "Tentar novamente",
                    aoAcao = vm::carregar
                )
            }

            is DetalheUiState.NaoEncontrado -> {
                EstadoTela(
                    icone = Icons.Outlined.SearchOff,
                    titulo = "Jogador não encontrado",
                    mensagem = "Não encontramos os dados deste jogador."
                )
            }

            is DetalheUiState.Sucesso -> {
                ConteudoDetalhe(
                    jogador = estado.jogador,
                    favorito = favorito,
                    aoAlternarFavorito = vm::alternarFavorito
                )
            }
        }
    }
}

@Composable
private fun ConteudoDetalhe(
    jogador: Jogador,
    favorito: Boolean,
    aoAlternarFavorito: () -> Unit
) {
    Spacer(Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        FotoJogador(jogador.foto, jogador.nome, 76.dp)
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                text = jogador.nome,
                color = OnzeCores.Texto,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
            if (jogador.nomeCompleto != null) {
                Text(
                    text = jogador.nomeCompleto,
                    color = OnzeCores.TextoSecundario,
                    fontSize = 12.sp
                )
            }
        }
    }

    Spacer(Modifier.height(20.dp))
    val forma = RoundedCornerShape(14.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(OnzeCores.Superficie, forma)
            .border(1.dp, OnzeCores.Borda, forma)
            .padding(16.dp)
    ) {
        LinhaDupla(
            rotuloEsquerda = "Nacionalidade",
            valorEsquerda = traduzirPais(jogador.nacionalidade),
            rotuloDireita = "Posição",
            valorDireita = traduzirPosicao(jogador.posicao)
        )
        Divisor()
        LinhaDupla(
            rotuloEsquerda = "Clube",
            valorEsquerda = jogador.clube ?: "Sem clube",
            rotuloDireita = "Número da camisa",
            valorDireita = jogador.numero ?: "—"
        )
        Divisor()
        LinhaDupla(
            rotuloEsquerda = "Data de nascimento",
            valorEsquerda = formatarData(jogador.nascimento)
        )
    }

    Spacer(Modifier.height(20.dp))
    if (favorito) {
        BotaoSecundario(
            texto = "Favoritado",
            aoClicar = aoAlternarFavorito,
            icone = Icons.Filled.Favorite
        )
    } else {
        BotaoOnze(
            texto = "Favoritar",
            aoClicar = aoAlternarFavorito,
            icone = Icons.Outlined.FavoriteBorder
        )
    }

    Spacer(Modifier.height(16.dp))
    Text(
        text = "Dados e foto: TheSportsDB",
        color = OnzeCores.TextoTerciario,
        fontSize = 11.sp,
        modifier = Modifier.padding(bottom = 24.dp)
    )
}

@Composable
private fun Divisor() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = OnzeCores.Borda
    )
}

@Composable
private fun LinhaDupla(
    rotuloEsquerda: String,
    valorEsquerda: String,
    rotuloDireita: String? = null,
    valorDireita: String? = null
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Informacao(rotuloEsquerda, valorEsquerda, Modifier.weight(1f))
        if (rotuloDireita != null && valorDireita != null) {
            Informacao(rotuloDireita, valorDireita, Modifier.weight(1f))
        }
    }
}

@Composable
private fun Informacao(rotulo: String, valor: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(rotulo, color = OnzeCores.TextoSecundario, fontSize = 11.sp)
        Spacer(Modifier.height(2.dp))
        Text(valor, color = OnzeCores.Texto, fontWeight = FontWeight.SemiBold)
    }
}
