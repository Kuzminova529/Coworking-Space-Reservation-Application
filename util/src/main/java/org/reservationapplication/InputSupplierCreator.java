package org.reservationapplication;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InputSupplierCreator<T, K> {
    protected Consumer<String> consumer;
    protected Supplier<K> supplier;
    protected Function<K, T> converter;

    public InputSupplierCreator(Consumer<String> consumer, Supplier<K> supplier, Function<K, T> converter) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.converter = converter;
    }

    public InputSupplierCreator(Consumer<String> consumer, Supplier<K> supplier) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.converter = s -> (T) s;
    }

    public Supplier<T> supplier(String message) {
        consumer.accept(message);
        return ()-> converter.apply(supplier.get());
    }
}
