package com.temp.tractivetest.data

import javax.inject.Inject
import kotlin.random.Random

class PetCalls @Inject constructor() {
    //this class would likely get an assisted injected BluetoothGatt or GattService to perform calls

    //This method stands for a bleConnection.readCharacteristic call and returns a value on callback
    suspend fun getPosition() : CallResult<Int>{
        return CallResult.success(Random.nextInt(0, 30))
    }
}