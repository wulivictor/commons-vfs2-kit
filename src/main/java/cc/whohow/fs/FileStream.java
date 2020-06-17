package cc.whohow.fs;

import cc.whohow.fs.util.UncheckedCloseable;

import java.io.Closeable;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface FileStream<F> extends Iterable<F>, Closeable {
    default Stream<F> stream() {
        return StreamSupport.stream(spliterator(), false)
                .onClose(new UncheckedCloseable(this));
    }
}