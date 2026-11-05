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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onze.app.data.CampoAuth
import com.onze.app.userinterface.theme.OnzeCores
import com.onze.app.viewmodel.CadastroViewModel
import com.onze.app.viewmodel.Fabricas

@Composable
fun CadastroScreen(
    aoVoltar: () -> Unit,
    aoCadastrar: () -> Unit,
    modifier: Modifier = Modifier,
    vm: CadastroViewModel = viewModel(factory = Fabricas.cadastro)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        TextButton(onClick = aoVoltar, contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = OnzeCores.TextoSecundario, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(6.dp))
            Text("Voltar", color = OnzeCores.TextoSecundario)
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Crie sua conta",
            color = OnzeCores.Texto,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Seu espaço para descobrir e salvar jogadores.",
            color = OnzeCores.TextoSecundario,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(28.dp))

        CampoOnze(
            rotulo = "Nome",
            valor = vm.nome,
            aoMudar = vm::aoMudarNome,
            dica = "Seu nome",
            erro = vm.erros[CampoAuth.NOME],
            capitalizacao = KeyboardCapitalization.Words
        )
        Spacer(Modifier.height(14.dp))
        CampoOnze(
            rotulo = "E-mail",
            valor = vm.email,
            aoMudar = vm::aoMudarEmail,
            dica = "Seu e-mail",
            erro = vm.erros[CampoAuth.EMAIL],
            tipoTeclado = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        CampoOnze(
            rotulo = "Senha",
            valor = vm.senha,
            aoMudar = vm::aoMudarSenha,
            dica = "Crie uma senha",
            erro = vm.erros[CampoAuth.SENHA],
            ehSenha = true
        )
        Spacer(Modifier.height(14.dp))
        CampoOnze(
            rotulo = "Confirmar senha",
            valor = vm.confirmacao,
            aoMudar = vm::aoMudarConfirmacao,
            dica = "Repita sua senha",
            erro = vm.erros[CampoAuth.CONFIRMACAO],
            ehSenha = true,
            acaoIme = ImeAction.Done,
            aoAcaoIme = { vm.cadastrar(aoCadastrar) }
        )

        Spacer(Modifier.height(24.dp))
        BotaoOnze(
            texto = "Cadastrar",
            aoClicar = { vm.cadastrar(aoCadastrar) },
            carregando = vm.carregando
        )

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Já tem uma conta?", color = OnzeCores.TextoSecundario, fontSize = 13.sp)
            TextButton(onClick = aoVoltar) {
                Text("Voltar ao login", color = OnzeCores.Verde, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
