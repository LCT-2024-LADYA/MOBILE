package ru.gozerov.fitladya.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.gozerov.fitladya.app.CacheConstants

@InstallIn(SingletonComponent::class)
@Module
interface CacheModule {

    companion object {

        @Provides
        fun provideSharedPrefs(@ApplicationContext context: Context) =
            context.getSharedPreferences(CacheConstants.LADYA_SHARED_PREFS, Context.MODE_PRIVATE)

    }

}