package edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.geremy_delossantos_p2_ap2.remote.dto.ContributorDto
import edu.ucne.geremy_delossantos_p2_ap2.remote.dto.RepositoryDto
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.*

@Composable
fun ApiListScreen(
    state: ApiUIState,
    onCreate: () -> Unit,
    onItemClick: (RepositoryDto) -> Unit,
    onLoadContributors: (String, String) -> Unit,
    contributors: List<ContributorDto>,
    isContributorLoading: Boolean,
    contributorError: String?,
) {
    var searchQuery by remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        @OptIn(FlowPreview::class)
        snapshotFlow { searchQuery }
            .debounce(400)
            .collectLatest { debouncedQuery = it }
    }

    val filteredList = if (debouncedQuery.isBlank()) {
        state.api
    } else {
        state.api.filter {
            it.name.contains(debouncedQuery, ignoreCase = true) ||
                    it.description?.contains(debouncedQuery, ignoreCase = true) == true ||
                    it.htmlUrl.contains(debouncedQuery, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate,
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(paddingValues)
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Text(
                text = "Lista de Repositorios",
                style = TextStyle(
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            val fuenteDatos = when {
                state.isLoading -> "Cargando..."
                state.errorMessage != null -> "Mostrando datos locales (Room)"
                else -> "Mostrando datos desde API"
            }

            val fuenteColor = when {
                state.isLoading -> Color.DarkGray
                state.errorMessage != null -> Color.Red
                else -> Color(0xFF388E3C)
            }

            Text(
                text = fuenteDatos,
                color = fuenteColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(top = 24.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar Repositorio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    Text("Cargando...", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                state.errorMessage != null -> {
                    Text(
                        text = state.errorMessage,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        items(filteredList) { repo ->
                            RepositoryRow(
                                repo = repo,
                                onClick = {
                                    onItemClick(repo)
                                    onLoadContributors("enelramon", repo.name)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Contribuyentes:",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    ContributorList(
                        contributors = contributors,
                        isLoading = isContributorLoading,
                        error = contributorError
                    )
                }
            }
        }
    }
}


@Composable
fun ContributorList(
    contributors: List<ContributorDto>,
    isLoading: Boolean,
    error: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Contribuyentes",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when {
            isLoading -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            contributors.isEmpty() -> {
                Text(
                    text = "No hay contribuyentes",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 250.dp)
                ) {
                    items(contributors) { contributor ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray.copy(alpha = 0.2f))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = contributor.login,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Contribuciones: ${contributor.contributions}",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RepositoryRow(
    repo: RepositoryDto,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(22.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = repo.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = repo.description ?: "Sin descripción",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
            Text(
                text = repo.htmlUrl,
                fontSize = 14.sp,
                color = Color.Blue
            )
        }
    }
}
