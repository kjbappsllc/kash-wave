package io.kw.service.streaming;

import io.kw.service.order.OrderCommand;
import org.apache.commons.chain.impl.ChainBase;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class StreamingService extends ChainBase {
    @Inject
    Instance<StreamingCommand> streamingCommands;

    @PostConstruct
    public void init() {
        for (StreamingCommand streamingCommand : streamingCommands) {
            System.out.println("Adding Command: " + streamingCommand);
            addCommand(streamingCommand);
        }
    }
}
