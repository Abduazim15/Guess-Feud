package com.skipissue.guess.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skipissue.guess.api.GameAPI
import com.skipissue.guess.model.VariantResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameAPI: GameAPI
) : ViewModel() {
    private val _successFlow = MutableSharedFlow<List<VariantResponse>>()
    val stateSuccess: SharedFlow<List<VariantResponse>> = _successFlow

    private val _errorFlow = MutableSharedFlow<Int>()
    val errorFlow: SharedFlow<Int> = _errorFlow

    private val _networkFlow = MutableSharedFlow<Unit>()
    val networkFlow: SharedFlow<Unit> = _networkFlow


    fun getAutoCompletes(query: String) {
        viewModelScope.launch {
            val response = gameAPI.getSuggestion(query, "wt-wt")
            if (response.isSuccessful && response.body() != null){
                _successFlow.emit(response.body()!!)
            }
        }
    }

}