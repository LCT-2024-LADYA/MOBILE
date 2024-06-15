package ru.gozerov.fitladya.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.data.cache.LoginStorageImpl
import ru.gozerov.data.cache.TrainingStorage
import ru.gozerov.data.cache.TrainingStorageImpl
import ru.gozerov.data.repositories.ChatRepositoryImpl
import ru.gozerov.data.repositories.LoginRepositoryImpl
import ru.gozerov.data.repositories.ServiceRepositoryImpl
import ru.gozerov.data.repositories.TrainingRepositoryImpl
import ru.gozerov.domain.repositories.ChatRepository
import ru.gozerov.domain.repositories.LoginRepository
import ru.gozerov.domain.repositories.ServiceRepository
import ru.gozerov.domain.repositories.TrainingRepository
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

    @Binds
    @Singleton
    fun bindTrainingRepoImplToTrainingRepo(trainingRepositoryImpl: TrainingRepositoryImpl): TrainingRepository

    @Binds
    @Singleton
    fun bindTrainingStorageImplToTrainingRepo(trainingStorageImpl: TrainingStorageImpl): TrainingStorage

    @Binds
    @Singleton
    fun bindChatRepoImplToChatRepo(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    fun bindServiceRepoImplToServiceRepo(serviceRepositoryImpl: ServiceRepositoryImpl): ServiceRepository

}