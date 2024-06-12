package ru.gozerov.fitladya.di

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.gozerov.data.api.ApiConstants.BASE_URL
import ru.gozerov.data.api.ChatApi
import ru.gozerov.data.api.LoginApi
import ru.gozerov.data.api.TrainingApi
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RetrofitModule {

    companion object {

        @Singleton
        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()

        @Singleton
        @Provides
        fun provideLoginApi(retrofit: Retrofit): LoginApi = retrofit.create(LoginApi::class.java)

        @Singleton
        @Provides
        fun provideTrainingApi(retrofit: Retrofit): TrainingApi =
            retrofit.create(TrainingApi::class.java)

        @Singleton
        @Provides
        fun provideChatApi(retrofit: Retrofit): ChatApi =
            retrofit.create(ChatApi::class.java)
    }


}