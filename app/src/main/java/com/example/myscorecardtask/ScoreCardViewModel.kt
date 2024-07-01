@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myscorecardtask

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ScoreCardViewModel : ViewModel() {

    var selectedFilter by mutableStateOf("Company")
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> get() = _searchText
    private val _bottomSheetState = MutableStateFlow(SheetValue.Hidden)
    val bottomSheetState: StateFlow<SheetValue> = _bottomSheetState.asStateFlow()


    fun showBottomSheet() {
        _bottomSheetState.value = SheetValue.Expanded
    }


    fun hideBottomSheet() {
        _bottomSheetState.value = SheetValue.Hidden
    }


    fun onFilterChanged(newFilter: String) {
        selectedFilter = newFilter
        Log.d("SelectedFilter", selectedFilter)
    }

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
    }

}