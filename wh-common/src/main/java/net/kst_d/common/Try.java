package net.kst_d.common;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class Try<T> {
    private final T success;
    private final Throwable failure;

    private Try(T success, Throwable failure) {
	this.success = success;
	this.failure = failure;
    }

    public static <T> Try<T> success(T value) {
	return new Try<>(value, null);
    }

    public static <T> Try<T> failure(Throwable throwable) {
	return new Try<>(null, throwable);
    }

    public boolean isSuccess() {
	return success != null;
    }

    public boolean isFailure() {
	return failure != null;
    }

    public T get() {
	return success;
    }

    public Throwable reason() {
	return failure;
    }

    public static <T> Try<T> of(Callable<T> action) {
	try {
	    return success(action.call());
	} catch (Throwable throwable) {
	    return failure(throwable);
	}
    }

    public <R> Try<R> map(Function<T, R> function) {
	if (isSuccess()) {
	    try {
		return success(function.apply(get()));
	    } catch (Throwable throwable) {
		return failure(throwable);
	    }
	} else {
	    return (Try<R>) this;
	}
    }

/*
// код не вполне корректный: потенциально теряется исключение
    public <R> Try<R> map(Function<T, R> function, Consumer<T> closer) {
	if (isSuccess()) {
	    try {
		return success(function.apply(get()));
	    } catch (Throwable throwable) {
		return failure(throwable);
	    } finally {
		try {
		    closer.accept(get());
		} catch (Throwable throwable) {
		    return failure(throwable);
		}
	    }
	} else {
	    return (Try<R>) this;
	}
    }
*/


    @Override
    public String toString() {
	if (isSuccess()) {
	    return "Success{" + success + "}";
	} else {
	    return "Failure{" + failure + "}";
	}
    }
}
