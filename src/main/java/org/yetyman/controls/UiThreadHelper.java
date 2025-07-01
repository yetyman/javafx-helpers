package org.yetyman.controls;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class UiThreadHelper {
    private static Logger log = LoggerFactory.getLogger(UiThreadHelper.class);

    private static final String UI_THREAD_NAME = "JavaFX Application Thread";

    public static Future<Object> runOnUiThreadAsync(Runnable runnable) {
        if(Thread.currentThread().getName().equals(UI_THREAD_NAME)) {
            runnable.run();
            return CompletableFuture.completedFuture(null);
        } else {
            CompletableFuture<Object> future = new CompletableFuture<>();
            Platform.runLater(futureRunnable(future, runnable));
            return future;
        }
    }
    public static <T> Future<T> runOnUiThreadAsync(Supplier<T> supplier) {
        if(Thread.currentThread().getName().equals(UI_THREAD_NAME)) {
            return CompletableFuture.completedFuture(supplier.get());
        } else {
            CompletableFuture<T> future = new CompletableFuture<>();
            Platform.runLater(futureSupplier(future, supplier));
            return future;
        }
    }
    public static void runOnUiThreadSync(Runnable runnable) {
        if(Thread.currentThread().getName().equals(UI_THREAD_NAME)) {
            runnable.run();
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(latchRunnable(latch, runnable));
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static <T> T runOnUiThreadSync(Supplier<T> supplier) {
        if(Thread.currentThread().getName().equals(UI_THREAD_NAME)) {
            return supplier.get();
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<T> value = new AtomicReference<>(null);
            Platform.runLater(latchSupplier(latch, value, supplier));
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return value.get();
        }
    }

    private static Runnable latchRunnable(CountDownLatch latch, Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.warn("Could not complete runnable on ui thread", e);
            } finally {
                latch.countDown();
            }
        };
    }
    private static <T> Runnable latchSupplier(CountDownLatch latch, AtomicReference<T> value, Supplier<T> supplier) {
        return () -> {
            try {
                value.set(supplier.get());
            } catch (Exception e) {
                log.warn("Could not complete runnable on ui thread", e);
            } finally {
                latch.countDown();
            }
        };
    }
    private static Runnable futureRunnable(CompletableFuture<Object> future, Runnable runnable) {
        return () -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        };
    }
    private static <T> Runnable futureSupplier(CompletableFuture<T> future, Supplier<T> runnable) {
        return () -> {
            try {
                future.complete(runnable.get());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        };
    }
}
