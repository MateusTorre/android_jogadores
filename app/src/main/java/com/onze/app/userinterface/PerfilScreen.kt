package com.onze.app.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onze.app.userinterface.theme.OnzeCores
import com.onze.app.viewmodel.Fabricas
import com.onze.app.viewmodel.SessaoViewModel

@Composable
fun PerfilScreen(
    aoSair: () -> Unit,
    modifier: Modifier = Modifier,
    vm: SessaoViewModel = viewModel(factory = Fabricas.sessao)
) {
    val usuario by vm.usuario.collectAsStateWithLifecycle()
    var confirmandoSaida by rememberSaveable { mutableStateOf(false) }
    val nome = usuario?.nome.orEmpty()
    val email = usuario?.email.orEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        OnzeLogo()
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Perfil",
            color = OnzeCores.Texto,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Sua conta no Onze.",
            color = OnzeCores.TextoSecundario,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(OnzeCores.VerdeEscuro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(siglaDe(nome), color = OnzeCores.Verde, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(nome, color = OnzeCores.Texto, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("Minha conta", color = OnzeCores.TextoSecundario, fontSize = 12.sp)
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
            Text("Nome", color = OnzeCores.TextoSecundario, fontSize = 11.sp)
            Text(nome, color = OnzeCores.Texto, fontWeight = FontWeight.SemiBold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = OnzeCores.Borda)
            Text("E-mail", color = OnzeCores.TextoSecundario, fontSize = 11.sp)
            Text(email, color = OnzeCores.Texto, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(20.dp))
        BotaoSecundario(
            texto = "Sair",
            aoClicar = { confirmandoSaida = true },
            icone = Icons.AutoMirrored.Filled.ExitToApp
        )
    }

    if (confirmandoSaida) {
        AlertDialog(
            onDismissRequest = { confirmandoSaida = false },
            containerColor = OnzeCores.Superficie,
            titleContentColor = OnzeCores.Texto,
            textContentColor = OnzeCores.TextoSecundario,
            title = { Text("Sair da sua conta?", fontWeight = FontWeight.Bold) },
            text = { Text("Você precisará entrar novamente para acessar seus favoritos.") },
            dismissButton = {
                OutlinedButton(onClick = { confirmandoSaida = false }) {
                    Text("Cancelar", color = OnzeCores.Texto)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        confirmandoSaida = false
                        vm.sair()
                        aoSair()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OnzeCores.Verde,
                        contentColor = OnzeCores.SobreVerde
                    )
                ) {
                    Text("Sair", fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}
