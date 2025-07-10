package edu.ucne.geremy_delossantos_p2_ap2.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema.ApiViewModel
import edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema.ApiListScreen
import edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema.ApiScreen


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
            val contributors by apiViewModel.contributors.collectAsState()
            val isContributorLoading by apiViewModel.isLoadingContributors.collectAsState()
            val contributorError by apiViewModel.contributorsError.collectAsState()

            ApiListScreen(
                state = uiState,
                onCreate = {
                    navHostController.navigate("Api/null")
                },
                onItemClick = { repo ->
                    apiViewModel.getContributors("enelramon", repo.name)
                },
                contributors = contributors,
                isContributorLoading = isContributorLoading,
                contributorError = contributorError,
                onLoadContributors = { owner, repo ->
                    apiViewModel.getContributors(owner, repo)
                }
            )
        }

        composable("Api/{repoName}", arguments = listOf(navArgument("repoName") { nullable = true })) { backStackEntry ->
            val uiState by apiViewModel.uiState.collectAsState()
            val contributors by apiViewModel.contributors.collectAsState()
            val isContributorLoading by apiViewModel.isLoadingContributors.collectAsState()
            val contributorError by apiViewModel.contributorsError.collectAsState()

            ApiScreen(
                state = uiState,
                onSave = { name, description, htmlUrl ->
                    apiViewModel.save(name, description, htmlUrl)
                    navHostController.popBackStack()
                },
                onCancel = {
                    navHostController.popBackStack()
                },
                onGetContributors = { name ->
                    apiViewModel.getContributors("enelramon", name)
                },
                contributors = contributors,
                isLoading = isContributorLoading,
                error = contributorError
            )
        }
    }
}


