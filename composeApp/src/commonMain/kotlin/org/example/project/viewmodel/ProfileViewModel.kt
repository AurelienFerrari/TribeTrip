package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    
    private val _userName = MutableStateFlow("Utilisateur")
    val userName: StateFlow<String> = _userName.asStateFlow()

    fun updateName(newName: String) {
        _userName.value = newName
    }
}
