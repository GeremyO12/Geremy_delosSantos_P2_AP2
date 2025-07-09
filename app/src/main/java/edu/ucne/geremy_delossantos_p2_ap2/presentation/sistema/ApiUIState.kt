package edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema

import edu.ucne.geremy_delossantos_p2_ap2.remote.dto.RepositoryDto

data class ApiUIState (
    val name: String = "",
    val description: String = "",
    val htmlUrl: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val api: List<RepositoryDto> = emptyList(),
    val inputError: String? = null
)