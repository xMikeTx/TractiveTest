package com.temp.tractivetest.data

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlin.random.Random

class PetCalls @Inject constructor() {
    //this class would likely get an assisted injected BluetoothGatt or GattService to perform calls

    //This method stands for a bleConnection.readCharacteristic call and returns a value on callback
    suspend fun getPosition() : CallResult<Int>{
        return CallResult.success(Random.nextInt(0, 30))
    }

    var inputs: String? = null
    var name: String = ""
    fun getPet(): Pet {
        val url = URL("http://www.tractive-server.com/pet")
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

        var j = JSONObject(inputs)

        if (j.getString("name") == null){
            name = ""
        }else if(j.getString("name").length > 5){
            name = j.getString("name").take(5)
        }


        return Pet(name, j.getString("image"))
    }

    class Pet constructor(var name: String, var image: String) {


    }
}