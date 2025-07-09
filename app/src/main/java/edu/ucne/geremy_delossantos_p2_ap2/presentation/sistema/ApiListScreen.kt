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
import androidx.compose.ui.tooling.preview.Preview
import edu.ucne.geremy_delossantos_p2_ap2.remote.dto.RepositoryDto
import kotlinx.coroutines.flow.*

@Composable
fun ApiListScreen(
    state: ApiUIState,
    onCreate: () -> Unit,
    onItemClick: (RepositoryDto) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
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
                                onClick = { onItemClick(repo) }
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

@Preview(showBackground = true)
@Composable
fun RepositoryListScreenPreview() {
    val sampleRepos = remember {
        mutableStateListOf(
            RepositoryDto(name = "Repository1", description = "Primer Repositorio", htmlUrl = "https://github.com/repo1"),
            RepositoryDto(name = "Repository2", description = "Segundo Repositorio", htmlUrl = "https://github.com/repo2"),
            RepositoryDto(name = "Repository3", description = null, htmlUrl = "https://github.com/repo3")
        )
    }
    val state = ApiUIState(
        api = sampleRepos
    )

    ApiListScreen(
        state = state,
        onCreate = { sampleRepos.add(
            RepositoryDto(name = "NuevoRepo", description = "Descripción nueva", htmlUrl = "https://github.com/nuevo")
        ) },
        onItemClick = { }
    )
}