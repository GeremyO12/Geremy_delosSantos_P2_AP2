package edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.geremy_delossantos_p2_ap2.data.repository.ApiRepository
import edu.ucne.geremy_delossantos_p2_ap2.remote.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiUIState())
    val uiState: StateFlow<ApiUIState> get() = _uiState

    init {
        getApi()
    }

    fun getApi() {
        viewModelScope.launch {
            repository.getApi().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            api = result.data ?: emptyList(),
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
}