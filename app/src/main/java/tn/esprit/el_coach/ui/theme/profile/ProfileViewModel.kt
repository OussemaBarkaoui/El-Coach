package tn.esprit.el_coach.ui.theme.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _profileImageUrl = MutableStateFlow<String?>(null) // URL de l'image
    val profileImageUrl: StateFlow<String?> = _profileImageUrl

    fun updateProfileImage(url: String) {
        viewModelScope.launch {
            _profileImageUrl.emit(url) // Mettre à jour l'image
        }
    }
}
