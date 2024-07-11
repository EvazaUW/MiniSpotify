package com.evaza.spotify.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaza.spotify.datamodel.Section
import com.evaza.spotify.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// HomeViewModel:
// state store （维护数据）
// handle event

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
): ViewModel(){ // viewModel在转屏的时候，数据不会被丢掉，状态是可持续维护的。
    // （如果不使用MVVM，Google viewModel,发生 configuration change时，需要自己另外存数据。ViewModal是一个推荐方案）

    private val _uiState : MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState(true, listOf()))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Event
    fun fetchHomeScreen() {
        viewModelScope.launch {
            val sections: List<Section> = repository.getHomeSections()
            _uiState.value = HomeUiState(isLoading = false, feed = sections)
            // return HomeUiState(false, sections)
            Log.d("HomeViewModel", _uiState.value.toString())
        }
    }
}

data class HomeUiState (
    val isLoading: Boolean,
    val feed: List<Section>
)