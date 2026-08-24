package br.com.afya.composebasico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.afya.composebasico.ui.theme.ComposeBasicoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeBasicoTheme {
                CartaoAluno(
                    nome = "Acadêmico Afya ADS",
                    matricula = "4o Período - Dispositivos Móveis",
                    status = "matriculado",
                    onCardClick = {},
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(
    texto: String,
    modifier: Modifier = Modifier,
    corFundo: Color = Color(0xFFCE0058)
) {
    Text(
        text = texto.uppercase(),
        color = Color.White,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .background(
                color = corFundo,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun StatusBadgePreview() {
    ComposeBasicoTheme {
        StatusBadge(
            texto = "matriculado",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun PerfilHeader(
    nome: String,
    matricula: String,
    modifier: Modifier = Modifier,
    avatarAluno: ImageVector = Icons.Default.AccountCircle
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = avatarAluno,
            contentDescription = "Avatar do Aluno",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            tint = Color(0xFFCE0058)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = nome,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = matricula,
                color = Color(0xFF75787B),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PerfilHeaderPreview() {
    ComposeBasicoTheme {
        PerfilHeader(
            nome = "João Guilherme Brasil Pichetti",
            matricula = "2026-ADS-04 - 4o Período",
            avatarAluno = Icons.Default.AccountBox
        )
    }
}

@Composable
fun CardBotoesAcao(
    primaryButtonText: String,
    onPrimaryButtonClick: () -> Unit,
    secondaryButtonText: String,
    onSecondaryButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        OutlinedButton(onClick = onSecondaryButtonClick) {
            Text(secondaryButtonText)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onPrimaryButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFCE0058)
            )
        ) {
            Text(primaryButtonText)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardBotoesAcaoPreview() {
    ComposeBasicoTheme {
        CardBotoesAcao(
            primaryButtonText = "Acessar Frequência",
            onPrimaryButtonClick = {},
            secondaryButtonText = "Acessar Plano de Aula",
            onSecondaryButtonClick = {}
        )
    }
}

@Composable
fun CartaoAluno(
    nome: String,
    matricula: String,
    status: String,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onPrimaryButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onCardClick() },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                PerfilHeader(
                    nome = nome,
                    matricula = matricula
                )
                Spacer(modifier = Modifier.height(16.dp))
                CardBotoesAcao(
                    secondaryButtonText = "Detalhes",
                    onSecondaryButtonClick = onSecondaryButtonClick,
                    primaryButtonText = "Acessar Notas",
                    onPrimaryButtonClick = onPrimaryButtonClick
                )
            }
        }
        StatusBadge(
            texto = status,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartaoAlunoPreview() {
    ComposeBasicoTheme {
        CartaoAluno(
            nome = "Acadêmico Afya ADS",
            matricula = "4o Período - Dispositivos Móveis",
            status = "matriculado",
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}