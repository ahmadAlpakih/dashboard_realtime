package com.github.ahmad.hellospring.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;


    @JsonProperty("status")
    private HttpStatus status;

    @JsonProperty("error")
    private ApiError error;

    @JsonProperty("data")
    private Object data;

}
