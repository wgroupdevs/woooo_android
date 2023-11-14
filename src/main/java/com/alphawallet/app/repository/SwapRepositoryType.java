package eu.siacs.conversations.Repository;

import com.alphawallet.app.entity.lifi.SwapProvider;

import java.util.List;

public interface SwapRepositoryType
{
    List<SwapProvider> getProviders();
}
