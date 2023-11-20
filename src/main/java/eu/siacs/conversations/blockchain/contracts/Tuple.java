package eu.siacs.conversations.blockchain.contracts;

public class Tuple<T1, T2> {
    private final T1 value1;
    private final T2 value2;

    public Tuple(T1 value1, T2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T1 getValue1() {
        return value1;
    }

    public T2 getValue2() {
        return value2;
    }
}
