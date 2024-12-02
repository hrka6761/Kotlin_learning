package ir.hrka.kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.core.Constants.BASE_URL
import ir.hrka.kotlin.data.datasource.github.GithubAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideGithubApi(retrofit: Retrofit): GithubAPI = retrofit.create(GithubAPI::class.java)
}