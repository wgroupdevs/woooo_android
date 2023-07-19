package woooo_app.woooo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import woooo_app.woooo.data.repositoryImp.AuthRepositoryImpl
import woooo_app.woooo.domain.repository.AuthRepository


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository
}