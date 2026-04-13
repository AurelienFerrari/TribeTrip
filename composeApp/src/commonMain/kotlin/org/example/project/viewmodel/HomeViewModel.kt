package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    
    private val _title = MutableStateFlow("Accueil")
    val title: StateFlow<String> = _title.asStateFlow()

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }
}