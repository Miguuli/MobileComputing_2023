package com.example.core.domain.businesslogic

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Configuration.*
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class DomainModule: Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Singleton
    @Provides
    fun provideContext(): CoroutineContext {
        return CoroutineScope(Dispatchers.IO + SupervisorJob()).coroutineContext
    }

    @Singleton
    @Provides
    fun provideConfiguration(): Configuration {
        return workManagerConfiguration
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}