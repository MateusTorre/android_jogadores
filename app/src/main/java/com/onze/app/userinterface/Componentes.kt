package com.onze.app.userinterface

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.onze.app.data.Jogador
import com.onze.app.userinterface.theme.OnzeCores

@Composable
fun OnzeLogo(modifier: Modifier = Modifier, tamanho: TextUnit = 22.sp) {
    Text(
        text = buildAnnotatedString {
            append("Onze")
            withStyle(SpanStyle(color = OnzeCores.Verde)) { append(".") }
        },
        modifier = modifier,
        fontSize = tamanho,
        fontWeight = FontWeight.ExtraBold,
        color = OnzeCores.Texto
    )
}

@Composable
fun CampoOnze(
    rotulo: String,
    valor: String,
    aoMudar: (String) -> Unit,
    dica: String,
    modifier: Modifier = Modifier,
    ehSenha: Boolean = false,
    erro: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    acaoIme: ImeAction = ImeAction.Next,
    aoAcaoIme: () -> Unit = {},
    iconeInicial: ImageVector? = null,
    limpavel: Boolean = false,
    capitalizacao: KeyboardCapitalization = KeyboardCapitalization.None
) {
    var senhaVisivel by remember { mutableStateOf(false) }
    val foco = LocalFocusManager.current

    Column(modifier) {
        Text(
            text = rotulo,
            color = OnzeCores.TextoSecundario,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = aoMudar,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(dica, color = OnzeCores.TextoTerciario) },
            singleLine = true,
            isError = erro != null,
            leadingIcon = if (iconeInicial != null) {
                { Icon(iconeInicial, contentDescription = null, tint = OnzeCores.TextoSecundario) }
            } else null,
            trailingIcon = {
                if (ehSenha) {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                            tint = OnzeCores.TextoSecundario
                        )
                    }
                } else if (limpavel && valor.isNotEmpty()) {
                    IconButton(onClick = { aoMudar("") }) {
                        Icon(Icons.Filled.Close, contentDescription = "Limpar", tint = OnzeCores.TextoSecundario)
                    }
                }
            },
            visualTransformation = if (ehSenha && !senhaVisivel) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                capitalization = capitalizacao,
                keyboardType = if (ehSenha) KeyboardType.Password else tipoTeclado,
                imeAction = acaoIme
            ),
            keyboardActions = KeyboardActions(
                onNext = { foco.moveFocus(FocusDirection.Down) },
                onDone = {
                    foco.clearFocus()
                    aoAcaoIme()
                },
                onSearch = { aoAcaoIme() },
                onGo = { aoAcaoIme() }
            ),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = OnzeCores.Texto,
                unfocusedTextColor = OnzeCores.Texto,
                focusedContainerColor = OnzeCores.SuperficieAlta,
                unfocusedContainerColor = OnzeCores.SuperficieAlta,
                errorContainerColor = OnzeCores.SuperficieAlta,
                focusedBorderColor = OnzeCores.Verde,
                unfocusedBorderColor = OnzeCores.Borda,
                errorBorderColor = OnzeCores.Erro,
                cursorColor = OnzeCores.Verde
            )
        )
        if (erro != null) {
            Text(
                text = erro,
                color = OnzeCores.Erro,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun BotaoOnze(
    texto: String,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    carregando: Boolean = false,
    habilitado: Boolean = true,
    icone: ImageVector? = null
) {
    Button(
        onClick = aoClicar,
        enabled = habilitado && !carregando,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = OnzeCores.Verde,
            contentColor = OnzeCores.SobreVerde,
            disabledContainerColor = OnzeCores.Verde.copy(alpha = 0.4f),
            disabledContentColor = OnzeCores.SobreVerde.copy(alpha = 0.7f)
        )
    ) {
        if (carregando) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = OnzeCores.SobreVerde
            )
        } else {
            if (icone != null) {
                Icon(icone, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(texto, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun BotaoSecundario(
    texto: String,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    icone: ImageVector? = null
) {
    OutlinedButton(
        onClick = aoClicar,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, OnzeCores.Borda),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = OnzeCores.SuperficieAlta,
            contentColor = OnzeCores.Verde
        )
    ) {
        if (icone != null) {
            Icon(icone, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(texto, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun FotoJogador(url: String?, nome: String, tamanho: Dp, modifier: Modifier = Modifier) {
    val sigla: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OnzeCores.SuperficieAlta),
            contentAlignment = Alignment.Center
        ) {
            Text(siglaDe(nome), color = OnzeCores.Verde, fontWeight = FontWeight.Bold)
        }
    }

    Box(
        modifier = modifier
            .size(tamanho)
            .clip(RoundedCornerShape(12.dp))
    ) {
        if (url.isNullOrBlank()) {
            sigla()
        } else {
            SubcomposeAsyncImage(
                model = url,
                contentDescription = nome,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(OnzeCores.SuperficieAlta)
                    )
                },
                error = { sigla() }
            )
        }
    }
}

@Composable
fun CartaoJogador(
    jogador: Jogador,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    acao: @Composable () -> Unit
) {
    val forma = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(OnzeCores.Superficie)
            .border(1.dp, OnzeCores.Borda, forma)
            .clickable(onClick = aoClicar)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FotoJogador(jogador.foto, jogador.nome, 52.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = jogador.nome,
                color = OnzeCores.Texto,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = traduzirPosicao(jogador.posicao),
                color = OnzeCores.TextoSecundario,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = jogador.clube ?: "Sem clube",
                color = OnzeCores.TextoSecundario,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        acao()
    }
}

@Composable
fun CartaoEsqueleto(modifier: Modifier = Modifier) {
    val transicao = rememberInfiniteTransition(label = "esqueleto")
    val alfa by transicao.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "alfa"
    )
    val cor = OnzeCores.SuperficieAlta.copy(alpha = alfa)
    val forma = RoundedCornerShape(14.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(OnzeCores.Superficie)
            .border(1.dp, OnzeCores.Borda, forma)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cor)
        )
        Spacer(Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.width(140.dp).height(12.dp).clip(CircleShape).background(cor))
            Box(Modifier.width(90.dp).height(10.dp).clip(CircleShape).background(cor))
            Box(Modifier.width(110.dp).height(10.dp).clip(CircleShape).background(cor))
        }
    }
}

@Composable
fun EstadoTela(
    icone: ImageVector,
    titulo: String,
    mensagem: String,
    modifier: Modifier = Modifier,
    rotuloAcao: String? = null,
    aoAcao: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(OnzeCores.SuperficieAlta),
            contentAlignment = Alignment.Center
        ) {
            Icon(icone, contentDescription = null, tint = OnzeCores.TextoSecundario, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = titulo,
            color = OnzeCores.Texto,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = mensagem,
            color = OnzeCores.TextoSecundario,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
        if (rotuloAcao != null && aoAcao != null) {
            Spacer(Modifier.height(20.dp))
            BotaoSecundario(texto = rotuloAcao, aoClicar = aoAcao)
        }
    }
}
