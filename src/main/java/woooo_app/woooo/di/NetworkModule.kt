package woooo_app.woooo.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import woooo_app.woooo.data.datasource.remote.auth.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request().newBuilder()
            request.addHeader("Authorization", "<Your token here>")
            val actualRequest = request.build()
            it.proceed(actualRequest)
        }
    }


    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient = OkHttpClient.Builder().run {
        provideInterceptor()
        connectTimeout(50, TimeUnit.SECONDS)
        readTimeout(50, TimeUnit.SECONDS)
        build()
    }

    @Provides
    @Singleton
    fun providesApiService(okHttpClient: OkHttpClient, gson: Gson): AuthApiService = Retrofit
        .Builder()
        .run {
            baseUrl(AuthApiService.BASE_URL)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create(gson))
            build()
        }.create(AuthApiService::class.java)

}
