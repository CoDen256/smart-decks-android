package coden.decks.android.mvc.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionalInterfaces {

    public static <U> Consumer<U> acceptOrSkip(Consumer<U> callback){
        return acceptOrCall(callback, () -> {});
    }

    public static <U> Consumer<U> acceptOrCall(Consumer<U> callback, Runnable fallback){
        return (U element) -> {
            if (element == null){
                fallback.run();
                return;
            }
            callback.accept(element);
        };
    }

    public static <U, T> Function<U, T> applyOrSkip(Function<U, T> callback){
        return applyOrCall(callback, () -> {});
    }

    public static <U, T> Function<U, T> applyOrCall(Function<U, T> callback, Runnable fallback){
        return (U element) -> {
            if (element == null){
                fallback.run();
                return null;
            }
            return callback.apply(element);
        };
    }
}
