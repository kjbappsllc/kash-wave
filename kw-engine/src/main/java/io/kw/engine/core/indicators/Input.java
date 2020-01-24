package io.kw.engine.core.indicators;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Input <T> {
    private String name;
    private String value;
    private Class<T> caster;
}
