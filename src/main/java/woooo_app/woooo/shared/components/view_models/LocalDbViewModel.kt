package woooo_app.woooo.shared.components.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.siacs.conversations.persistance.DatabaseBackend
import javax.inject.Inject

@HiltViewModel
class LocalDbViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private var mDatabaseBackend: DatabaseBackend

    init {
        mDatabaseBackend = DatabaseBackend.getInstance(context);
    }


    fun clearLocalDatabase() {
        mDatabaseBackend.clearDatabase()
    }


}