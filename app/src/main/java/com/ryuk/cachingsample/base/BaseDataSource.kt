package com.ryuk.cachingsample.base

import android.accounts.NetworkErrorException
import android.content.Context
import com.ryuk.cachingsample.R
import com.ryuk.cachingsample.base.exception.NoConnectivityException
import com.ryuk.cachingsample.base.model.RestClientResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

abstract class BaseDataSource {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): RestClientResult<T> {
        return try {
            val response = call()
            return if (response.isSuccessful) {
                val body = response.body()
                if (body != null)
                    RestClientResult.success(body)
                else customError(context.getString(R.string.something_went_wrong))
            } else customError(context.getString(R.string.something_went_wrong))
        } catch (e: Exception) {
            when (e) {
                is NoConnectivityException -> {
                    customError(context.getString(R.string.please_check_your_internet_connection))
                }
                is SocketTimeoutException -> {
                    customError(context.getString(R.string.please_check_your_internet_connection))
                }
                is NetworkErrorException -> {
                    customError(context.getString(R.string.please_check_your_internet_connection))
                }
                is SSLHandshakeException -> {
                    customError(context.getString(R.string.please_check_your_internet_connection))
                }
                is UnknownHostException -> {
                    customError(context.getString(R.string.please_check_your_internet_connection))
                }
                is CancellationException -> {
                    customError("")
                }
                else ->
                    customError(e.message ?: e.toString())
            }
        }
    }

    private fun <T> customError(
        message: String,
        errorCode: Int? = null,
        errorBody: ResponseBody? = null,
        data: T? = null,
    ): RestClientResult<T> {
        return RestClientResult.error(
            message,
            data = data,
            errorCode = errorCode,
            errorBody = errorBody
        )
    }
}