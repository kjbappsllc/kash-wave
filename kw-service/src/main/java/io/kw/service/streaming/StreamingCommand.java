package io.kw.service.streaming;

import io.vavr.control.Try;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class StreamingCommand implements Command {

    private ExecutorService priceFeedExecutor;

    public StreamingCommand() {
        priceFeedExecutor = Executors.newSingleThreadExecutor();
    }

    protected StreamingContext getStreamingContext(Context context) {
        if (!(context instanceof StreamingContext)) return null;
        return (StreamingContext) context;
    }

    protected void runAsyncFeed(Runnable feedRunner) {
        priceFeedExecutor.submit(feedRunner);
    }

    protected void endExecutor() {
        Try.run(() -> {
            priceFeedExecutor.shutdown();
            priceFeedExecutor.awaitTermination(1, TimeUnit.SECONDS);
        }).andFinally(() -> {
            if (!priceFeedExecutor.isTerminated()) System.err.println("Canceling non-finished tasks");
            priceFeedExecutor.shutdownNow();
        });
    }
}
