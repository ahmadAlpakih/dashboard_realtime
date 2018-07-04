package com.github.ahmad.hellospring.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int errorCode;
    private String message;
}
