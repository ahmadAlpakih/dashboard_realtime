package com.github.ahmad.hellospring.bean;

import com.kastkode.springsandwich.filter.api.BeforeHandler;
import com.kastkode.springsandwich.filter.api.Flow;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestrictByRole implements BeforeHandler {
    @Override
    public Flow handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HandlerMethod handlerMethod, String[] flags) throws Exception {
        System.out.println("RestrictByRole logic executed, checking for these roles");
        if(flags != null) {
            for(String arg:flags) {
                System.out.println(arg);
            }
        }

        //or return Flow.HALT to halt this request and prevent execution of the controller
        //you may also wish to redirect to a login page here
        return Flow.HALT;
    }
}
