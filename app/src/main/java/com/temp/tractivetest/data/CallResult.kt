package com.temp.tractivetest.data

/**
 * class representing formatted data call results from an asynchronous call.
 * instances of this class cannot be modified as they will not be able to notify listeners
 * @param status call status
 * @param data actual data
 * @param extra extra information such as headers which are not part of main data
 * @param code call response code
 * @param message call message
 */
@Suppress("DataClassPrivateConstructor")
data class CallResult<out T> private constructor(val status: Status,
                                                 val data: T?,
                                                 val extra : Map<String,String> = emptyMap(),
                                                 val code : Int? = null,
                                                 val message: String? = null) {

    enum class Status {
        IDLE,
        SUCCESS,
        ERROR,
        LOADING
    }

    /**
     * set of Helper methods to create call results of different states,
     */
    companion object {
        fun <T> idle(data: T?=null, message: String? = null, code: Int? = null): CallResult<T> {
            return CallResult(Status.IDLE, data, emptyMap(), code, message)
        }
        fun <T> success(data: T?, headers: Map<String,String> = emptyMap(),
                        message: String? = null, code: Int? = null): CallResult<T> {
            return CallResult(Status.SUCCESS, data, headers, code, message)
        }

        fun <T> error(message: String? = null, code: Int? = null, data : T? = null,
                      headers: Map<String,String> = emptyMap()): CallResult<T> {
            return CallResult(Status.ERROR, data, headers, code, message)
        }

        fun <T> loading(data: T? = null): CallResult<T> {
            return CallResult(Status.LOADING, data)
        }
    }

    /**
     * Helper method to convert data in a call result
     */
    fun <M> copyConvert(converter : (T?)->M?) =
            CallResult<M>(status,converter(data), extra,code,message)

    fun isIdle() : Boolean =
        status == Status.IDLE

    fun isSuccess() : Boolean =
        status == Status.SUCCESS

    fun isLoading() : Boolean =
        status == Status.LOADING

    fun isFail() : Boolean =
        status == Status.ERROR
}