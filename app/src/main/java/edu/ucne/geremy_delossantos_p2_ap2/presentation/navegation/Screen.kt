package edu.ucne.geremy_delossantos_p2_ap2.presentation.navegation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object ApiListScreen : Screen()
    @Serializable
    data class ApiScreen(val name: String?) : Screen()
}