package com.apiintegration.hngstage1profileaggregator.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private T data;
    private String message;

    public ApiResponse( T data,String message) {
        this.status = "success";
        this.data = data;
        this.message = message;
    }
    public ApiResponse( T data) {
        this.status = "success";
        this.data = data;
    }

}
