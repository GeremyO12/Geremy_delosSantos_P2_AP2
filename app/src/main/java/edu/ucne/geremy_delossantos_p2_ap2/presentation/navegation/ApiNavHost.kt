package edu.ucne.geremy_delossantos_p2_ap2.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema.ApiViewModel
import edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema.ApiListScreen


@Composable
fun ApiNavHost(
    navHostController: NavHostController,
    apiViewModel: ApiViewModel = hiltViewModel()
) {
    NavHost(
        navController = navHostController,
        startDestination = "ApiList"
    ) {
        composable("ApiList") {
            val uiState by apiViewModel.uiState.collectAsState()

            ApiListScreen(
                state = uiState,
                onCreate = {
                    navHostController.navigate("Api/null")
                },
                onItemClick = { repo ->
                    navHostController.navigate("Api/${repo.name}")
                }
            )
        }

    }
}
