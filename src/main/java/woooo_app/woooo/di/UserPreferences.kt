package woooo_app.woooo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woooo_app.woooo.data.datasource.local.UserPreferences
import woooo_app.woooo.data.datasource.local.UserPreferencesImpl
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object UserPreferences {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile("user_data") }
        )

    }


    @Provides
    fun provideUserPreferences(dataStore: DataStore<Preferences>): UserPreferences = UserPreferencesImpl(dataStore)
}