package ru.gozerov.fitladya.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.data.cache.LoginStorageImpl
import ru.gozerov.data.repositories.LoginRepositoryImpl
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface AppBindModule {

    @Binds
    @Singleton
    fun bindLoginRepoImplToLoginRepo(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    fun bindLoginStorageImplToLoginRepo(loginStorageImpl: LoginStorageImpl): LoginStorage

}