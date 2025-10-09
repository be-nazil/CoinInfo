package com.nb.coininfo.di

import androidx.annotation.Keep
import com.nb.coininfo.BuildConfig
import com.nb.coininfo.data.apiservices.ApiInterface
import com.nb.coininfo.data.apiservices.CoinService2
import com.nb.coininfo.data.apiservices.CoinsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import kotlin.apply
import kotlin.jvm.java
import kotlin.run

@Module
@InstallIn(SingletonComponent::class)
@Keep
object AppModule {

    @Provides
    @Singleton
    fun provideApiKey(): String = BuildConfig.API_KEY

    @Provides
    @Named(DINameConstants.DI_NAME_COIN_PAPRIKA_HTTP)
    fun provideOkHttpClientCoinPaprika(): OkHttpClient {
        val okClientBuilder = OkHttpClient.Builder().run {
            addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })
        }

        val loggingInterceptorHeader = HttpLoggingInterceptor()
        loggingInterceptorHeader.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val loggingInterceptorBody = HttpLoggingInterceptor()
        loggingInterceptorBody.setLevel(HttpLoggingInterceptor.Level.BODY)
        okClientBuilder.apply {
            addInterceptor(loggingInterceptorHeader)
            addInterceptor(loggingInterceptorBody)
        }
        return okClientBuilder.build()
    }

    @Provides
    @Named(DINameConstants.DI__HTTP)
    fun provideOkHttpClient(): OkHttpClient {
        val okClientBuilder = OkHttpClient.Builder().run {
            addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("x-cg-demo-api-key", provideApiKey())
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })
        }

        val loggingInterceptorHeader = HttpLoggingInterceptor()
        loggingInterceptorHeader.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val loggingInterceptorBody = HttpLoggingInterceptor()
        loggingInterceptorBody.setLevel(HttpLoggingInterceptor.Level.BODY)
        okClientBuilder.apply {
            addInterceptor(loggingInterceptorHeader)
            addInterceptor(loggingInterceptorBody)
        }
        return okClientBuilder.build()
    }

    @Provides
    @Named(DINameConstants.DI_NAME_COIN_PAPRIKA_RETROFIT_SERVICE)
    fun provideRetrofitCoinPaprika(
        @Named(DINameConstants.DI_NAME_COIN_PAPRIKA_HTTP) okHttpClientCoinPaprika: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.coinpaprika.com/v1/")
            .client(okHttpClientCoinPaprika)
            .build()
    }

    @Provides
    @Named(DINameConstants.DI_NAME_BASE_RETROFIT_SERVICE)
    fun provideRetrofit(
        @Named(DINameConstants.DI__HTTP)
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.coingecko.com/api/v3/")
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(
        @Named(DINameConstants.DI_NAME_COIN_PAPRIKA_RETROFIT_SERVICE) provideRetrofitCoinPaprika: Retrofit
    ): ApiInterface {
        return provideRetrofitCoinPaprika.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinServices(
        @Named(DINameConstants.DI_NAME_COIN_PAPRIKA_RETROFIT_SERVICE) provideRetrofitCoinPaprika: Retrofit
    ): CoinsService {
        return provideRetrofitCoinPaprika.create(CoinsService::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinServices2(
        @Named(DINameConstants.DI_NAME_BASE_RETROFIT_SERVICE) provideRetrofit: Retrofit
    ): CoinService2 {
        return provideRetrofit.create(CoinService2::class.java)
    }

}

object DINameConstants {
    const val DI_NAME_COIN_PAPRIKA_RETROFIT_SERVICE = "CoinPaprikaRetrofitService"
    const val DI_NAME_COIN_PAPRIKA_HTTP = "CoinPaprikaHTTP"
    const val DI__HTTP = "HTTP"
    const val DI_NAME_BASE_RETROFIT_SERVICE = "RetrofitService"
    const val API_KEY = "CG-zCmDsgVVqrFbQEmtodSSy2qj"
}