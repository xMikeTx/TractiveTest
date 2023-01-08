package com.temp.tractivetest.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * A flow wrapper with a refresh method that re-initiates the wrapped flow
 */
class RefreshFlow<T>(flowProvider: () -> Flow<T>) {
    private val controlFlow = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val data = controlFlow.flatMapLatest { flowProvider.invoke() }
    fun refresh() {
        controlFlow.value = !controlFlow.value
    }
}