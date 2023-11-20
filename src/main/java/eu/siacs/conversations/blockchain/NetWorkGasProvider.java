package eu.siacs.conversations.blockchain;

import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class NetWorkGasProvider extends StaticGasProvider {
    // Increase the GAS_LIMIT value
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(250000);

    // Increase the GAS_PRICE value
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(25_000_000_000L);

    public NetWorkGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }
}