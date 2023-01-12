package com.temp.tractivetest.data

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This singleton handles data requests as a single source using flow,
 *
 * @param api retrofit api instance
 */
@Singleton
class DataSource @Inject constructor(
    private val api: PetCalls,
) {
    companion object {
        const val DEFAULT_DELAY = 3000L
    }

    /**
     * Get Pet position from an api call
     * @return Flow that will update when data is available
     */
    fun getPetPosition(): Flow<CallResult<Int>> {
        return getInfiniteFlow {
            api.getPosition()
        }
    }

    fun getPet(): PetCalls.Pet {
        return api.getPet()
    }

    /**
     * Helper method that executes and emits api calls as a single Flow
     * @param apiCall method containing api call invocation
     * @return Flow that will report data state
     */
    private fun <T> getInfiniteFlow(
        delayMillis: Long = DEFAULT_DELAY,
        apiCall: suspend () -> CallResult<T>
    ): Flow<CallResult<T>> =
        flow<CallResult<T>> {
            try {
                coroutineScope {
                    while (true) {
                        delay(delayMillis)
                        val responseCall = async(Dispatchers.IO) { apiCall() }
                        val response = responseCall.await()
                        if (response.isSuccess() && response.data != null) {
                            emit(CallResult.success(response.data))
                        } else if (response.isFail()) {
                            emit(CallResult.error(response.message, response.code))
                            this@coroutineScope.cancel()
                        }
                    }
                }
            } catch (_: CancellationException) {
            }
        }.onStart { emit(CallResult.loading()) }

}