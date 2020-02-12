package io.kw.engine;

import akka.actor.ActorRef;
import io.kw.cdi.qualifiers.Actor;
import io.kw.engine.system.StreamingActor;
import io.kw.service.OrderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KWEngine {

    public static final String KW_SYSTEM = "kw-engine";

    @Inject
    @Actor(type = StreamingActor.class, associatedSystem = KWEngine.KW_SYSTEM)
    ActorRef streamingActor;

    String apiKey = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";

    public void startUp() {
        streamingActor.tell("CDI", ActorRef.noSender());
    }
}
