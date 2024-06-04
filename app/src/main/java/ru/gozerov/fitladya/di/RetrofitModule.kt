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
import ru.gozerov.data.api.LoginApi
import ru.gozerov.fitladya.app.ApiConstants.BASE_URL
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RetrofitModule {

    companion object {

        @Singleton
        @Provides
        fun provideLoginApi(okHttpClient: OkHttpClient): LoginApi = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .build()
            .create(LoginApi::class.java)

    }


}