package com.wgroup.woooo_app.woooo.di

import com.wgroup.woooo_app.woooo.data.repositoryImp.AuthRepositoryImpl
import com.wgroup.woooo_app.woooo.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository
}