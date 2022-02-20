package com.ryuk.cachingsample.util

import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount
import javax.inject.Inject

class GroupLastRequestStore @Inject constructor(
    private val requestKey: String,
    private val prefs: Prefs
) {

    suspend fun getRequestInstant(): Instant {
        return prefs.getLastRequestTime(requestKey)
    }

    suspend fun isRequestExpired(threshold: TemporalAmount): Boolean {
        return isRequestBefore(threshold.inPast())
    }

    suspend fun isRequestBefore(instant: Instant): Boolean {
        return getRequestInstant().isBefore(instant)
    }

    suspend fun updateLastRequest(timestamp: Instant = Instant.now()) {
        prefs.setLastRequestTime(requestKey, timestamp)
    }

    suspend fun invalidateLastRequest() = updateLastRequest(Instant.EPOCH)
}