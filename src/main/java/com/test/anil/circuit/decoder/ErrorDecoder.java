package com.test.anil.circuit.decoder;

import com.test.anil.Response;

public interface ErrorDecoder {

    Response decode();

    public static final ErrorDecoder DEFAULT_ERROR_DECODER = new DefaultErrorDecoder();


    static class DefaultErrorDecoder implements ErrorDecoder{
        @Override
        public Response decode() {
            return new Response(500,"The downstream service is not responding correctly.");
        }
    }
}
