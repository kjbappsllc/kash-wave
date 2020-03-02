package io.kw.service.streaming;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
}
