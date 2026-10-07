package com.pemob.resepnindya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemob.resepnindya.data.model.Meal
import com.pemob.resepnindya.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class RecipeViewModel : ViewModel() {
    private val repository = RecipeRepository()

    private val _homeState = MutableStateFlow<UiState<List<Meal>>>(UiState.Loading)
    val homeState: StateFlow<UiState<List<Meal>>> = _homeState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<Meal>>(UiState.Loading)
    val detailState: StateFlow<UiState<Meal>> = _detailState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        // Load default recipes on initialization (chicken is a good default)
        searchRecipes("chicken")
    }

    fun searchRecipes(query: String) {
        _searchQuery.value = query
        _homeState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.searchRecipes(query)
            result.onSuccess { meals ->
                _homeState.value = UiState.Success(meals)
            }.onFailure { error ->
                _homeState.value = UiState.Error(error.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun getRecipeDetails(id: String) {
        _detailState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.getRecipeDetails(id)
            result.onSuccess { meal ->
                _detailState.value = UiState.Success(meal)
            }.onFailure { error ->
                _detailState.value = UiState.Error(error.message ?: "Terjadi kesalahan")
            }
        }
    }
}
