package com.temp.tractivetest

import com.temp.tractivetest.data.CallResult
import com.temp.tractivetest.data.DataSource
import com.temp.tractivetest.data.PetCalls
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class DataSourceTest {
    private val api = mockk<PetCalls>()

    @Test
    fun testPetCall() {
        coEvery { api.getPosition() }.returns(CallResult.success(1))
        val datasource = DataSource(PetCalls())
        val result = datasource.getPetPosition()
        runBlocking {
            result.collect {
                assertEquals(CallResult.Status.SUCCESS, it.status)
                assertEquals(1, it.data)
            }
        }
    }
}