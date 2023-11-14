package com.alphawallet.app.util

import eu.siacs.conversations.BuildConfig
import timber.log.Timber

object TimberInit
{
    @JvmStatic
    fun configTimber()
    {
        if (BuildConfig.DEBUG)
        {
            Timber.plant(Timber.DebugTree())
        }
        else
        {
            Timber.plant(ReleaseTree())
        }
    }
}
