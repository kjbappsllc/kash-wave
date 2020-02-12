package io.kw.cdi.qualifiers;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
@Target({TYPE, CONSTRUCTOR, FIELD, METHOD})
public @interface ActorSystemName {
    @Nonbinding String name();
    String DEFAULT_NAME = "system";
}
