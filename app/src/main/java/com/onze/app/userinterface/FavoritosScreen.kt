package com.onze.app.userinterface

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onze.app.userinterface.theme.OnzeCores
import com.onze.app.viewmodel.Fabricas
import com.onze.app.viewmodel.FavoritosViewModel

@Composable
fun FavoritosScreen(
    aoAbrirDetalhe: (String) -> Unit,
    modifier: Modifier = Modifier,
    vm: FavoritosViewModel = viewModel(factory = Fabricas.favoritos)
) {
    val favoritos by vm.favoritos.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        OnzeLogo()
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Favoritos",
            color = OnzeCores.Texto,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Seus jogadores, sempre por perto.",
            color = OnzeCores.TextoSecundario,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))

        val lista = favoritos
        when {
            lista == null -> {
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = OnzeCores.Verde)
                }
            }

            lista.isEmpty() -> {
                EstadoTela(
                    icone = Icons.Outlined.FavoriteBorder,
                    titulo = "Você ainda não salvou nenhum jogador",
                    mensagem = "Busque um jogador em Explorar e toque em Favoritar para salvá-lo aqui."
                )
            }

            else -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Jogadores salvos", color = OnzeCores.Texto, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (lista.size == 1) "1 jogador" else "${lista.size} jogadores",
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
                    items(lista, key = { it.id }) { jogador ->
                        CartaoJogador(
                            jogador = jogador,
                            aoClicar = { aoAbrirDetalhe(jogador.id) }
                        ) {
                            Column(
                                modifier = Modifier
                                    .clickable { vm.remover(jogador) }
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = "Remover dos favoritos",
                                    tint = OnzeCores.TextoSecundario,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text("Remover", color = OnzeCores.TextoSecundario, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
