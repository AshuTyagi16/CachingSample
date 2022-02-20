package com.ryuk.cachingsample.base.model

import okhttp3.ResponseBody

data class RestClientResult<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
    val errorCode: Int? = null,
    val errorBody: ResponseBody? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): RestClientResult<T> {
            return RestClientResult(Status.SUCCESS, data, null)
        }

        fun <T> loading(): RestClientResult<T> {
            return RestClientResult(Status.LOADING)
        }

        fun <T> error(
            message: String,
            data: T? = null,
            errorCode: Int? = null,
            errorBody: ResponseBody? = null
        ): RestClientResult<T> {
            return RestClientResult(
                Status.ERROR,
                message = message,
                errorCode = errorCode,
                errorBody = errorBody
            )
        }
    }
}