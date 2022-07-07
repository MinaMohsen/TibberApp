package com.example.tibberapp.presentation.powerupslist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tibberapp.R
import com.example.tibberapp.domain.model.AssignmentData
import com.example.tibberapp.domain.usecase.GetPowerUpsUseCase
import com.example.tibberapp.util.Resource
import com.example.tibberapp.presentation.util.UiText
import com.example.tibberapp.util.Constants.NO_INTERNET_CONNECTION_ERROR_CODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PowerUpsViewModel @Inject constructor(
    private val getPowerUpsUseCase: GetPowerUpsUseCase
) : ViewModel() {

    val isRefreshing = mutableStateOf(false)
    val powerUpsList = mutableStateOf<List<AssignmentData>>(listOf())
    val isLoading = mutableStateOf(false)
    val loadError = mutableStateOf<UiText>(UiText.DynamicString(""))

    init {
        loadPowerUps()
    }

    fun loadPowerUps() {
        isLoading.value = true
        viewModelScope.launch {
            getPowerUpsUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        isLoading.value = false
                        isRefreshing.value = false
                        powerUpsList.value =
                            result.data?.sortedByDescending { it.connected } ?: emptyList()
                    }
                    is Resource.Error -> {
                        isLoading.value = false
                        isRefreshing.value = false
                        result.message?.let {
                            if (result.errorCode == NO_INTERNET_CONNECTION_ERROR_CODE)
                                loadError.value =
                                    UiText.StringResource(resId = R.string.check_internet)
                            else
                                loadError.value = UiText.DynamicString(it)
                        } ?: run {
                            loadError.value =
                                UiText.StringResource(resId = R.string.error_unexpected_message)
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        isRefreshing.value = true
        loadPowerUps()
    }

}