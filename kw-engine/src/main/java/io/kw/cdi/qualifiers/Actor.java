package io.kw.cdi.qualifiers;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
@Target({TYPE, CONSTRUCTOR, FIELD, METHOD})
public @interface Actor {
    @Nonbinding Class<? extends akka.actor.Actor> type();
    @Nonbinding String associatedSystem() default ActorSystem.DEFAULT_NAME;
}
