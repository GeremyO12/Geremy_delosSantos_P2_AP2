package edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiScreen(
    state: ApiUIState,
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(state.name) }
    var description by remember { mutableStateOf(state.description) }
    var htmlUrl by remember { mutableStateOf(state.htmlUrl) }
    var error by remember { mutableStateOf<String?>(state.inputError) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registrar/Editar Repositorio",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(padding)
                .padding(20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.95f), shape = MaterialTheme.shapes.medium)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del repositorio") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = htmlUrl,
                    onValueChange = { htmlUrl = it },
                    label = { Text("Url del repositorio") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                )

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            when {
                                name.isBlank() -> error = "El nombre es requerido"
                                htmlUrl.isBlank() -> error = "La URL es requerida"
                                else -> {
                                    error = null
                                    onSave(name, description, htmlUrl)
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApiScreenPreview() {
    ApiScreen(
        state = ApiUIState(),
        onSave = { name, description, htmlUrl -> println("Nuevo repo: $name, $description, $htmlUrl") },
        onCancel = { println("Cancelado") }
    )
}