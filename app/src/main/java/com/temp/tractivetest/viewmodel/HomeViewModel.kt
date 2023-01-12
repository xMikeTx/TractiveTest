package com.temp.tractivetest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.temp.tractivetest.data.DataSource
import com.temp.tractivetest.util.RefreshFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.abs

/**
 * ViewModel for Home page
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dataSource: DataSource
) : ViewModel() {
    companion object {
        const val MAX_PET_POS = 30
    }

    private val positionFLow = RefreshFlow {
        dataSource.getPetPosition()
    }

    //pet position call will return distance, we have to invert it to get proximity
    val position = positionFLow.data.map {
        it.copyConvert { number ->
            number?.let {
                abs(MAX_PET_POS - number)
            }
        }
    }

    val pet = dataSource.getPet()

    fun refresh() = positionFLow.refresh()
}