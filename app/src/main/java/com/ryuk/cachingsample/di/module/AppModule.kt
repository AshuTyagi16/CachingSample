package com.ryuk.cachingsample.di.module

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import com.dropbox.android.external.fs3.filesystem.FileSystemFactory
import com.ryuk.cachingsample.data.network.MovieService
import com.ryuk.cachingsample.data.repository.MovieRepository
import com.ryuk.cachingsample.domain.repository.MovieRepositoryImpl
import com.ryuk.cachingsample.domain.use_case.FetchPopularMovieUseCase
import com.ryuk.cachingsample.domain.use_case.impl.FetchPopularMovieUseCaseImpl
import com.ryuk.cachingsample.util.SharedPreferenceUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        @Singleton
        fun provideMovieService(retrofit: Retrofit): MovieService {
            return retrofit.create(MovieService::class.java)
        }

        @Provides
        @Singleton
        fun provideFileSystem(@ApplicationContext context: Context) =
            FileSystemFactory.create(context.cacheDir)

        @Provides
        @Singleton
        fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
            return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        @Provides
        @Singleton
        fun sharedPreferenceUtil(preferences: SharedPreferences): SharedPreferenceUtil {
            return SharedPreferenceUtil(preferences)
        }

        @Provides
        @Singleton
        fun preferences(@ApplicationContext context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

    @Binds
    @Singleton
    abstract fun provideMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

    @Binds
    @Singleton
    abstract fun provideFetchPopularMovieUseCase(fetchPopularMovieUseCaseImpl: FetchPopularMovieUseCaseImpl): FetchPopularMovieUseCase
}