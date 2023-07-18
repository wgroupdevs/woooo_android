package com.wgroup.woooo_app.woooo.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wgroup.woooo_app.woooo.data.datasource.remote.auth.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi
        .Builder()
        .run {
            add(KotlinJsonAdapterFactory())
            build()
        }

    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient = OkHttpClient.Builder().run {
        interceptors()
        connectTimeout(50, TimeUnit.SECONDS)
        readTimeout(50, TimeUnit.SECONDS)
        build()
    }

    @Provides
    @Singleton
    fun providesApiService(okHttpClient: OkHttpClient, moshi: Moshi): AuthApiService = Retrofit
        .Builder()
        .run {
            baseUrl(AuthApiService.BASE_URL)
            client(okHttpClient)
            addConverterFactory(MoshiConverterFactory.create(moshi))
            build()
        }.create(AuthApiService::class.java)

}
