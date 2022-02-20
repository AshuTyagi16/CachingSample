package com.ryuk.cachingsample.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor(
    private val sharedPreferenceUtil: SharedPreferenceUtil,
) {

    suspend fun getLastRequestTime(key: String): Instant {
        return withContext(Dispatchers.IO) {
            val epochMillis = sharedPreferenceUtil.getLong(key)
            Instant.ofEpochMilli(epochMillis)
        }
    }

    suspend fun setLastRequestTime(key: String, instant: Instant) {
        withContext(Dispatchers.IO) {
            val epochMillis = instant.toEpochMilli()
            sharedPreferenceUtil.putLong(key, epochMillis)
        }
    }


}