package com.atfotiad.composefoundry.core.di


import com.atfotiad.composefoundry.core.data.local.datastore.StorageManager
import com.atfotiad.composefoundry.features.counter.api.CounterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // Probably i will use Build.Configs
    private const val DEV_HOST = "dev-api.foundry.com"
    private const val STAGING_HOST = "staging-api.foundry.com"
    private const val PROD_HOST = "api.foundry.com"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        storageManager: StorageManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()

                // HOTSWAP LOGIC: Read current env from DataStore (Blocking for interceptor)
                val env = runBlocking { storageManager.apiEnvironment.first() }

                val newHost = when (env) {
                    "dev" -> DEV_HOST
                    "staging" -> STAGING_HOST
                    else -> PROD_HOST
                }

                val newUrl = request.url.newBuilder()
                    .host(newHost)
                    .build()

                chain.proceed(request.newBuilder().url(newUrl).build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                // Use BODY for debugging to see raw JSON, NONE for release
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        // We must tell Retrofit to treat "application/json" using our Json parser
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl("https://httpbin.org/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideCounterApi(retrofit: Retrofit): CounterApi {
        return retrofit.create(CounterApi::class.java)
    }

}
