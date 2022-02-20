package com.ryuk.cachingsample.util

import android.content.res.Resources
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.ryuk.cachingsample.base.model.RestClientResult
import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun TemporalAmount.inPast(): Instant = Instant.now().minus(this)

fun <T> LiveData<StoreResponse<RestClientResult<T>>>.observeCached(
    lifeCycleOwner: LifecycleOwner,
    onLoading: () -> Unit = {},
    onSuccess: (t: T) -> Unit,
    onError: (message: String) -> Unit = {},
    onSuccessWithNullData: () -> Unit = {},
) {
    this.observe(lifeCycleOwner) { response ->
        when (response) {
            is StoreResponse.Loading -> onLoading.invoke()
            is StoreResponse.Data -> {
                val result = response.value
                when (result.status) {
                    RestClientResult.Status.LOADING -> {
                        onLoading.invoke()
                    }
                    RestClientResult.Status.SUCCESS -> {
                        result.data?.let(onSuccess)
                            ?: run { onSuccessWithNullData.invoke() }
                    }
                    RestClientResult.Status.ERROR -> {
                        onError.invoke(
                            result.message.orGenericMessage()
                        )
                    }
                }
            }
            is StoreResponse.Error -> {
                if (response.origin == ResponseOrigin.Fetcher) {
                    onError.invoke(response.errorMessageOrNull().orGenericMessage())
                }
            }
            is StoreResponse.NoNewData -> {
                onSuccessWithNullData.invoke()
            }
        }
    }
}

fun String?.orGenericMessage() = this ?: "Something went wrong"

