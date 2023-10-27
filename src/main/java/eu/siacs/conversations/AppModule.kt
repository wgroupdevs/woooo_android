package eu.siacs.conversations

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.siacs.conversations.persistance.WOOPrefManager
import eu.siacs.conversations.ui.wallet.WalletViewModel
import io.metamask.androidsdk.Ethereum

@Module
@InstallIn(SingletonComponent::class)

internal object AppModule {
    @Provides
    fun provideEthereum(@ApplicationContext context: Context): Ethereum {
        return Ethereum(context)
    }

}
