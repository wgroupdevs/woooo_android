package com.alphawallet.app.entity;

/**
 * Created by Ehsan on 06/11/2023.
 */
public interface FragmentMessenger
{
    void tokenScriptError(String message);

    void playStoreUpdateReady(int versionUpdate);

    void externalUpdateReady(String version);
}
