package com.test.anil.circuit;

import com.test.anil.EnableCircuit;
import com.test.anil.circuit.decoder.ErrorDecoder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
public class CircuitWatcherAspect {

    private final Map<String, CircuitBreaker> circuitMap = new HashMap<String, CircuitBreaker>();

    @Pointcut(value = "@annotation(com.test.anil.EnableCircuit)")
    public void onEnableCircuit() {
    }

    @Around(value = "onEnableCircuit() && execution(**.*())")
    public Object watchCircuit(ProceedingJoinPoint joinPoint) {
        Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();
        String key = method.getName();
        CircuitBreaker circuitBreaker = circuitMap.computeIfAbsent(key, (k) -> createCircuit(k, method));
        try {
            if (circuitBreaker.isOpen() && !circuitBreaker.shouldRetry()) {
                return circuitBreaker.createResponse();
            }
            Object o = joinPoint.proceed();
            circuitBreaker.succeeded();
            return o;
        } catch (Throwable e) {
            circuitBreaker.failed();
            return circuitBreaker.createResponse();
        }
    }

    private CircuitBreaker createCircuit(String key, Method method) {
        EnableCircuit enableCircuit = method.getAnnotation(EnableCircuit.class);
        return new CircuitBreaker(key, enableCircuit.errorCount(), enableCircuit.retryAfter(), enableCircuit.successCount(),
                ErrorDecoder.DEFAULT_ERROR_DECODER);
    }

}
