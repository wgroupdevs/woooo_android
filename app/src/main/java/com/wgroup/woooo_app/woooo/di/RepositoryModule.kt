package com.wgroup.woooo_app.woooo.di

import com.wgroup.woooo_app.woooo.data.repository.AuthRepositoryImpl
import com.wgroup.woooo_app.woooo.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesMovieRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository


}