package com.zjj.authconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "enabled.autoConfituration", matchIfMissing = true)
public class MyAutoConfiguration {

    static {
        System.out.println("myAutoConfiguration init...");
    }

//    @Bean
//    public SimpleBean simpleBean(){
//        return new SimpleBean();
//    }

}
