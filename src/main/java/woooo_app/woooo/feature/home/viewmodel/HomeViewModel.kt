package woooo_app.woooo.feature.home.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    private val _displayName = mutableStateOf("")
    var displayname: State<String> = _displayName
    fun setDisplayName(displayName: String) {
        this._displayName.value = displayName
    }
    private val _avatar = mutableStateOf("")
    var avatar: State<String> = _avatar
    fun setAvatar(avatar: String) {
        this._avatar.value = avatar
    }

}