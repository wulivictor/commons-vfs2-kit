package cc.whohow.fs;

public interface Move<F1, F2> extends Command<F2> {
    F1 getSource();

    F2 getTarget();
}