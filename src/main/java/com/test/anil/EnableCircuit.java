package com.test.anil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface EnableCircuit {

    long retryAfter() default 1000;

    long errorCount() default 10;

    long successCount() default 3;

}
