package com.zjj.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PostConstructTest {

    @PostConstruct
    public void postConstruct() {
        System.out.println("init...");
    }
}
