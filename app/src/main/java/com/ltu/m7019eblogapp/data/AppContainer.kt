package com.ltu.m7019eblogapp.data

import com.ltu.m7019eblogapp.data.service.BlogApiService
import com.ltu.m7019eblogapp.utils.Constants

import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


interface AppContainer {
    val blogRepository: BlogRepository
}

// DefaultAppContainer implements the AppContainer interface and provides the concrete implementations of dependencies
class DefaultAppContainer : AppContainer {

    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Add an HTTP client logger for debugging purposes.
     */
    private fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    /**
     * Use the Retrofit builder to build a Retrofit object using a Moshi converter with our Moshi object.
     */
    private val retrofit = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.API_BASE_URL)
        .build()

    // Create an instance of the BlogApiService using the Retrofit object
    private val retrofitService: BlogApiService by lazy {
        retrofit.create(BlogApiService::class.java)
    }

    // Provide the concrete implementation of the blogRepository dependency
    override val blogRepository: BlogRepository by lazy {
        NetworkBlogRepository(retrofitService)
    }
}
