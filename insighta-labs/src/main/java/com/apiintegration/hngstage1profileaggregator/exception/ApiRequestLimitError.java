package com.apiintegration.hngstage1profileaggregator.exception;

public class ApiRequestLimitError extends ApiResponseException {
    public ApiRequestLimitError(String apiReturnedRequestLimitError) {
        super(apiReturnedRequestLimitError);
    }
}
