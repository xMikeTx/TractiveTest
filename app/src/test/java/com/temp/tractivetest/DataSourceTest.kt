package com.temp.tractivetest

import com.temp.tractivetest.data.CallResult
import com.temp.tractivetest.data.DataSource
import com.temp.tractivetest.data.PetCalls
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class DataSourceTest {
    private val api = mockk<PetCalls>()

    @Test
    fun testPetCall() {
        coEvery { api.getPosition() }.returns(CallResult.success(1))
        val datasource = DataSource(api)
        val result = datasource.getPetPosition()
        runBlocking {
            result.take(2).collectIndexed{ index, value ->
                if(index == 0) {
                    assertEquals(CallResult.Status.LOADING, value.status)
                }else {
                    assertEquals(CallResult.Status.SUCCESS, value.status)
                    assertEquals(1, value.data)
                }
            }
        }
    }
}