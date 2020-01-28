package io.kw.engine.strategies;

import lombok.*;

import java.util.UUID;

public abstract class Strategy {
    @Getter
    private UUID guid = UUID.randomUUID();
}
