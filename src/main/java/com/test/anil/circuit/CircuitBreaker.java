package com.test.anil.circuit;


import com.test.anil.circuit.decoder.ErrorDecoder;
import com.test.anil.Response;

import java.time.LocalDateTime;

public class CircuitBreaker {
    private final String key;
    private final long errorThreshold;
    private final long closeSuccessCount;
    private final long retryAfterSeconds;
    private final ErrorDecoder errorDecoder;

    public CircuitBreaker(String key, long errorThreshold, long closeSuccessCount, long retryAfterSeconds, ErrorDecoder errorDecoder) {
        this.key = key;
        this.errorThreshold = errorThreshold;
        this.closeSuccessCount = closeSuccessCount;
        this.retryAfterSeconds = retryAfterSeconds;
        this.errorDecoder = errorDecoder;
        this.status = CircuitStatus.CLOSED;
    }

    private int errorCount;
    private int successCount;
    private CircuitStatus status;
    private LocalDateTime circuitOpenAt;


    public boolean shouldRetry(){
        LocalDateTime allowedAfter = circuitOpenAt.plusSeconds(retryAfterSeconds);
        return LocalDateTime.now().isAfter(allowedAfter);
    }

    public boolean isOpen(){
        return status == CircuitStatus.OPEN;
    }

    private void markClosed(){
        reset();
    }

    public void succeeded(){
        if(isOpen()){
            successCount++;
            if(successCount>= closeSuccessCount){
                markClosed();
            }
        }
    }

    public void failed(){
        this.errorCount++;
        if(this.errorCount >= this.errorThreshold){
            openCircuit();
        }
    }

    private void openCircuit(){
        status = CircuitStatus.OPEN;
        circuitOpenAt = LocalDateTime.now();
    }

    private void reset(){
        this.status = CircuitStatus.CLOSED;
        this.errorCount = 0;
        this.successCount = 0;
        this.circuitOpenAt = null;
    }

    public Response createResponse(){
        return errorDecoder.decode();
    }

}
