package com.onze.app.userinterface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onze.app.userinterface.theme.OnzeCores
import com.onze.app.viewmodel.Fabricas
import com.onze.app.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    aoEntrar: () -> Unit,
    aoIrParaCadastro: () -> Unit,
    modifier: Modifier = Modifier,
    vm: LoginViewModel = viewModel(factory = Fabricas.login)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(48.dp))
        OnzeLogo(tamanho = 44.sp)
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Seu próximo craque está aqui",
            color = OnzeCores.Texto,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 32.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Entre para buscar jogadores e guardar seus favoritos.",
            color = OnzeCores.TextoSecundario,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(36.dp))

        CampoOnze(
            rotulo = "E-mail",
            valor = vm.email,
            aoMudar = vm::aoMudarEmail,
            dica = "Seu e-mail",
            tipoTeclado = KeyboardType.Email
        )
        Spacer(Modifier.height(16.dp))
        CampoOnze(
            rotulo = "Senha",
            valor = vm.senha,
            aoMudar = vm::aoMudarSenha,
            dica = "Sua senha",
            ehSenha = true,
            acaoIme = ImeAction.Done,
            aoAcaoIme = { vm.entrar(aoEntrar) }
        )

        if (vm.erro != null) {
            Text(
                text = vm.erro.orEmpty(),
                color = OnzeCores.Erro,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
        BotaoOnze(
            texto = "Entrar",
            aoClicar = { vm.entrar(aoEntrar) },
            carregando = vm.carregando
        )

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ainda não tem conta?", color = OnzeCores.TextoSecundario, fontSize = 13.sp)
            TextButton(onClick = aoIrParaCadastro) {
                Text("Cadastre-se", color = OnzeCores.Verde, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(32.dp))
        Text(
            text = "O futebol começa com uma boa descoberta.",
            color = OnzeCores.TextoTerciario,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
    }
}
