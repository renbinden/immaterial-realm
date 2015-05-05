package io.github.alyphen.immaterial_realm.server.event;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static io.github.alyphen.immaterial_realm.server.event.EventHandlerPriority.NORMAL;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface EventHandler {

    EventHandlerPriority priority() default NORMAL;
}
