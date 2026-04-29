package com.apiintegration.hngstage1profileaggregator.exception;

public class ApiResponseException extends ProfileAggregatorException {
    public ApiResponseException(String apiReturnedError) {
        super(apiReturnedError);
    }
}
