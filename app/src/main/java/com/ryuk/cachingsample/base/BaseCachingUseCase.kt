package com.ryuk.cachingsample.base

import com.dropbox.android.external.fs3.FileSystemPersister
import com.dropbox.android.external.fs3.PathResolver
import com.dropbox.android.external.fs3.filesystem.FileSystem
import com.dropbox.android.external.store4.*
import com.google.gson.Gson
import com.ryuk.cachingsample.base.model.RestClientResult
import com.ryuk.cachingsample.util.GroupLastRequestStore
import com.ryuk.cachingsample.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okio.Buffer
import org.threeten.bp.Duration
import java.nio.charset.StandardCharsets
import javax.inject.Inject

abstract class BaseCachingUseCase<T>(
    private val duration: Duration = Duration.ofHours(3),
    private var refresh: Boolean = false
) {

    @Inject
    lateinit var fileSystem: FileSystem

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var prefs: Prefs

    private val lastRequestStore by lazy { GroupLastRequestStore(cacheKey, prefs) }

    private val cacheKey: String = this.javaClass.name

    private val storageFileName by lazy { cacheKey.plus(".json") }

    abstract fun parseFromString(string: String): T

    abstract suspend fun fetchDataFromNetwork(): RestClientResult<T>

    private val fileSystemPersister by lazy {
        FileSystemPersister.create(
            fileSystem,
            object : PathResolver<String> {
                override fun resolve(key: String) = storageFileName
            }
        )
    }

    private val sourceOfTruth = SourceOfTruth.Companion.of<String,
            RestClientResult<T>,
            RestClientResult<T>>(
        nonFlowReader = {
            when {
                // If the request is expired, our data is stale
                lastRequestStore.isRequestExpired(duration) -> null

                // Otherwise, our data is fresh and valid
                else -> {
                    val data = runCatching {
                        fileSystemPersister.read(cacheKey)?.readString(
                            StandardCharsets.UTF_8
                        )
                            ?.let { parseFromString(it) }
                    }.getOrNull()
                    RestClientResult.success(data!!)
                }
            }
        },
        writer = { _, config: RestClientResult<T> ->
            if (config.status == RestClientResult.Status.SUCCESS && config.data != null) {
                val buffer = Buffer()
                withContext(Dispatchers.IO) {
                    buffer.writeUtf8(gson.toJson(config.data))
                }
                fileSystemPersister.write(cacheKey, buffer)
            }
        }
    )

    private val store: Store<String, RestClientResult<T>> by lazy {
        StoreBuilder
            .from(
                fetcher = Fetcher.of {
                    fetchDataFromNetwork().also {
                        lastRequestStore.updateLastRequest()
                    }
                },
                sourceOfTruth = sourceOfTruth
            )
            .build()
    }

    protected fun getDataFromStore() =
        store.stream(StoreRequest.cached(key = cacheKey, refresh = refresh))
            .flowOn(Dispatchers.Default)

    suspend fun clearCache() {
        //The only way to clear disk is to expire the request
        lastRequestStore.invalidateLastRequest()
    }
}