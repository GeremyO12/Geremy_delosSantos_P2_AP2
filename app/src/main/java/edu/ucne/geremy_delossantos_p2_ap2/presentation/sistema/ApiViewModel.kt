package edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.geremy_delossantos_p2_ap2.data.repository.ApiRepository
import edu.ucne.geremy_delossantos_p2_ap2.remote.dto.ContributorDto
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

    private val _contributors = MutableStateFlow<List<ContributorDto>>(emptyList())
    val contributors: StateFlow<List<ContributorDto>> get() = _contributors

    private val _isLoadingContributors = MutableStateFlow(false)
    val isLoadingContributors: StateFlow<Boolean> get() = _isLoadingContributors

    private val _contributorsError = MutableStateFlow<String?>(null)
    val contributorsError: StateFlow<String?> get() = _contributorsError

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

    fun save(name: String, description: String, htmlUrl: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            description = description,
            htmlUrl = htmlUrl
        )
    }

    fun getContributors(owner: String, repo: String) {
        viewModelScope.launch {
            repository.getContributors(owner, repo).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            contributors = result.data ?: emptyList(),
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

