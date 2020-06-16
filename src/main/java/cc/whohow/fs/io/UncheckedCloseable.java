package cc.whohow.fs.io;

import cc.whohow.fs.UncheckedException;

public class UncheckedCloseable implements Runnable {
    private final AutoCloseable closeable;

    public UncheckedCloseable(AutoCloseable closeable) {
        this.closeable = closeable;
    }

    @Override
    public void run() {
        try {
            closeable.close();
        } catch (Exception e) {
            throw UncheckedException.unchecked(e);
        }
    }
}
