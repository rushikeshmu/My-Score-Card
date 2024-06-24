@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myscorecardtask

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoreCardViewModel : ViewModel() {
    var selectedDate by mutableStateOf("01-06-2024")
    var selectedFilter by mutableStateOf("Company")
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> get() = _searchText
    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet


    fun onFilterChanged(newFilter: String) {
        selectedFilter = newFilter
        Log.d("SelectedFilter", selectedFilter)
    }

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
    }

}